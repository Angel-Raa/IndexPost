package io.github.angel.raa.utils;


import io.github.angel.raa.persistence.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String secretKey;
    @Value("${app.jwt.expiration.access}")
    private long accessExpiration;
    @Value("${app.jwt.expiration.refresh}")
    private long refreshExpiration;

    /**
     * Generado de clave
     */
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera el access token
     */
    public  String generateAccessToken (String email, Map<String, Object> extraClaims){
        return  generateToken(email, accessExpiration, extraClaims);
    }

    /**
     * Genera el refresh token
     */
    public String generateRefreshToken(String email, Map<String, Object> extraClaims){
        return generateToken(email, refreshExpiration, extraClaims);
    }

    /**
     * Genera el token
     * @param email correo de user
     * @param expiration tiempo de expiracion
     * @return token
     */
    private String generateToken(String email, long expiration, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Map<String, Object>  generateExtraClaims(User details){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", details.getUsername());
        extraClaims.put("roles",details.getRole());
        extraClaims.put("Authorities", details.getAuthorities());
        return extraClaims;
    }

    /**
     * Extrae el email
     */
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiracion
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Valida el token
     */
    public  boolean validateToken( final String token, final String email) {
        final String userEmail = extractEmail(token);
        return (userEmail.equals(email) && !isTokenExpired(token));
    }

}
