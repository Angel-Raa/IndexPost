package io.github.angel.raa.service.auth;

import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import io.github.angel.raa.dto.auth.Login;
import io.github.angel.raa.dto.auth.Register;

public interface AuthenticationService {
    AuthenticationResponse register(Register request);

    AuthenticationResponse login(Login request);
    AuthenticationResponse  verifyEmail(String verificationToken, String userId);
    AuthenticationResponse refreshToken(String refreshToken);
    AuthenticationResponse logout(String refreshToken);


}
