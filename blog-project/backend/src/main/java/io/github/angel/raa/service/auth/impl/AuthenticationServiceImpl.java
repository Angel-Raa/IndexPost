package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.dto.auth.Login;
import io.github.angel.raa.dto.auth.Register;
import io.github.angel.raa.excpetion.BadCredentialsException;
import io.github.angel.raa.excpetion.EmailAlreadyExistsException;
import io.github.angel.raa.excpetion.LockedException;
import io.github.angel.raa.excpetion.UserNotFoundException;
import io.github.angel.raa.persistence.entity.Role;
import io.github.angel.raa.persistence.entity.Token;
import io.github.angel.raa.persistence.entity.User;
import io.github.angel.raa.persistence.repository.RoleRepository;
import io.github.angel.raa.persistence.repository.TokenRepository;
import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.AccountUnlockService;
import io.github.angel.raa.service.auth.AuthenticationService;
import io.github.angel.raa.service.auth.AuthenticationVerificationService;
import io.github.angel.raa.service.auth.BruteForceProtectionService;
import io.github.angel.raa.utils.Authorities;
import io.github.angel.raa.utils.JwtUtils;
import io.github.angel.raa.utils.TokenType;
import io.github.angel.raa.utils.payload.AuthenticationResponse;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AuthenticationVerificationService authenticationVerificationService;
    private final JwtUtils jwtUtils;
    private final AccountUnlockService accountUnlockService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager manager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BruteForceProtectionService bruteForceProtectionService;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationVerificationService authenticationVerificationService, JwtUtils jwtUtils, AccountUnlockService accountUnlockService, UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository, AuthenticationManager manager, BCryptPasswordEncoder passwordEncoder, BruteForceProtectionService bruteForceProtectionService) {
        this.authenticationVerificationService = authenticationVerificationService;
        this.jwtUtils = jwtUtils;
        this.accountUnlockService = accountUnlockService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Transactional
    @Override
    public AuthenticationResponse register(Register request) throws MessagingException {
        final String email = request.getEmail().trim();
        final String password = request.getPassword().trim();
        final String name = request.getName();
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Crear y persistir el rol
        Role role = new Role();
        role.setAuthorities(Authorities.USER);
        roleRepository.persist(role);
        // Crear un nuevo usuario
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setVerified(false);
        user.setRoleId(role.getRoleId());
        userRepository.persist(user);

        String accessToken = jwtUtils.generateAccessToken(email, jwtUtils.generateExtraClaims(user));
        String refreshToken = jwtUtils.generateRefreshToken(email, jwtUtils.generateExtraClaims(user));
        authenticationVerificationService.sendVerificationEmailToUser(user.getUserId());
        Token token = new Token();
        token.setTokenValue(refreshToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        token.setExpiresAt(LocalDateTime.now().plusDays(7));
        token.setUserId(user.getUserId());
        tokenRepository.persist(token);


        return new AuthenticationResponse(accessToken, refreshToken, TokenType.BEARER, "Usuario registrado exitosamente. Por favor verifique su correo para confimar");
    }

    @Transactional
    @Override
    public AuthenticationResponse login(Login request) {
        log.info("Login request: {}", request);
        final String email = request.getEmail().trim();
        final String password = request.getPassword().trim();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        if (!user.isVerified()) {
            throw new UserNotFoundException("Usuario no verificado");
        }
        if (user.isAccountLocked()) {
            accountUnlockService.unlockAccount(email);
            throw new LockedException("Cuenta bloqueada. Por favor, contacte al administrador");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            bruteForceProtectionService.onAuthenticationFailure(user.getEmail());
            throw new BadCredentialsException("Credenciales incorrectas");


        }
        bruteForceProtectionService.onAuthenticationSuccess(email);
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userRepository.resetFailedAttempts(email);
        String accessToken = jwtUtils.generateAccessToken(email, jwtUtils.generateExtraClaims(user));
        String refreshToken = jwtUtils.generateRefreshToken(email, jwtUtils.generateExtraClaims(user));
        return new AuthenticationResponse(accessToken, refreshToken, TokenType.BEARER, "Login exitoso");

    }

    @Override
    public AuthenticationResponse verifyEmail(String verificationToken, String userId) {
        return null;
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public AuthenticationResponse logout(String refreshToken) {
        return null;
    }
}
