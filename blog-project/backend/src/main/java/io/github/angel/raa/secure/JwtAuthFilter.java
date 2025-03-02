package io.github.angel.raa.secure;

import io.github.angel.raa.service.auth.impl.CustomerUserDetailsService;
import io.github.angel.raa.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter  extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtUtils utils;
    private final CustomerUserDetailsService service;

    public JwtAuthFilter(HandlerExceptionResolver handlerExceptionResolver, JwtUtils utils, CustomerUserDetailsService service) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.utils = utils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(7);
            final String email = utils.extractEmail(token);
            if (email != null && utils.validateToken(token, email)){
                UserDetails details = service.loadUserByUsername(email);
                if(utils.validateToken(token, details.getUsername())){
                    UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken(
                            details.getUsername(),
                            null,
                            details.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
