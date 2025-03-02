package io.github.angel.raa.controller.auth;

import io.github.angel.raa.dto.auth.Register;
import io.github.angel.raa.service.auth.AuthenticationService;
import io.github.angel.raa.utils.payload.AuthenticationResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;


    public AuthenticationController(AuthenticationService service) {
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


}
