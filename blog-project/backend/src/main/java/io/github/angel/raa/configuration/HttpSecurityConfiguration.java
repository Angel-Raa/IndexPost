package io.github.angel.raa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class HttpSecurityConfiguration {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService service;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public HttpSecurityConfiguration(BCryptPasswordEncoder passwordEncoder, UserDetailsService service, CompromisedPasswordChecker compromisedPasswordChecker) {
        this.passwordEncoder = passwordEncoder;
        this.service = service;
        this.compromisedPasswordChecker = compromisedPasswordChecker;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(service);
        provider.setCompromisedPasswordChecker(compromisedPasswordChecker);
        return provider;
    }
}
