package io.github.angel.raa.utils.payload;

import io.github.angel.raa.utils.TokenType;

import java.io.Serial;
import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -1324687123687936281L;
    private String accessToken;
    private String refreshToken;
    private TokenType tokenType ;
    private String message;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String accessToken, String refreshToken, TokenType tokenType, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType=" + tokenType +
                ", message='" + message + '\'' +
                '}';
    }
}
