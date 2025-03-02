package io.github.angel.raa.service.auth;

import io.github.angel.raa.dto.auth.Login;
import io.github.angel.raa.dto.auth.Register;
import io.github.angel.raa.utils.payload.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(Register request);

    AuthenticationResponse login(Login request);
    AuthenticationResponse verifyEmail(String verificationToken, String userId);
    AuthenticationResponse refreshToken(String refreshToken);
    AuthenticationResponse logout(String refreshToken);


}
