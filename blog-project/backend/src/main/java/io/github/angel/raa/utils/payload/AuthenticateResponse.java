package io.github.angel.raa.utils.payload;

import io.github.angel.raa.utils.TokenType;

public class AuthenticateResponse {
    private String accessToken;
    private String refreshToken;
    private TokenType tokenType =  TokenType.BEARER;

    public AuthenticateResponse() {
    }

    public AuthenticateResponse(String refreshToken, String accessToken, TokenType tokenType) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
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
}
