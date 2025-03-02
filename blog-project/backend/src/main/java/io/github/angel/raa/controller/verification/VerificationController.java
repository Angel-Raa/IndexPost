package io.github.angel.raa.controller.verification;

import io.github.angel.raa.service.auth.AuthenticationVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private  final AuthenticationVerificationService service;

    public VerificationController(AuthenticationVerificationService service) {
        this.service = service;
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam(value = "token", name = "token", required = true) final String token) {

        return ResponseEntity.ok("Usuario verificado exitosamente");
    }
}
