package io.github.angel.raa.controller.auth;

import io.github.angel.raa.dto.auth.Login;
import io.github.angel.raa.dto.auth.Register;
import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.AuthenticationService;
import io.github.angel.raa.utils.payload.AuthenticationResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserRepository repository;
    private final AuthenticationService service;


    public AuthenticationController(UserRepository repository, AuthenticationService service) {
        this.repository = repository;
        this.service = service;

    }
    @PreAuthorize("permitAll")
    @GetMapping
    public String hello(){
        return "Hello World";
    }
    @PreAuthorize("permitAll")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody Register register) throws MessagingException {
        AuthenticationResponse response = service.register(register);
        log.info("Response: {}", response);
        return new ResponseEntity<>(response,  HttpStatus.OK);
    }

    @PreAuthorize("permitAll")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody Login login) {
        log.info("Login: {}", login);
        AuthenticationResponse response = service.login(login);
        log.info("Response de Login: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Transactional
    @PreAuthorize("permitAll")
    @GetMapping("/ejemplos")
    public ResponseEntity<String> ejemplos( ){
        int a = repository.findFailedAttempts("angelagueror23@gmail.com");
        //repository.lock("angelagueror23@gmail.com");
        //repository.increaseFailedAttempts("angelagueror23@gmail.com");
        repository.unLock("angelagueror23@gmail.com");
        return ResponseEntity.ok("Incremento " + a );
    }

}
