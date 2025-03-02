package io.github.angel.raa.service.auth;

import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import io.github.angel.raa.dto.auth.Login;
import io.github.angel.raa.dto.auth.Register;

public interface AuthenticationService {
    AuthenticationResponse register(Register request);

    AuthenticationResponse authenticate(Login request);
    AuthenticationResponse authenticate(String token);
    AuthenticationResponse authenticate(String token, String refreshToken);

}
