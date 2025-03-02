package io.github.angel.raa.controller.verification;

import io.github.angel.raa.service.auth.AuthenticationVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("permitAll")
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam(value = "token", name = "token") final String token) {
        if(!service.verifyUserEmail(token)){
            return ResponseEntity.badRequest().body("Token no valido");
        }
        return ResponseEntity.ok("Usuario verificado exitosamente. Puede cerrar la ventana");
    }
}
