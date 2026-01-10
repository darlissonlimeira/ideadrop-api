package io.github.darlissonlimeira.ideadrop.api.config;

import io.github.darlissonlimeira.ideadrop.api.exception.InvalidBearerTokenException;
import io.github.darlissonlimeira.ideadrop.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class BearerAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    private final HandlerExceptionResolver exceptionResolver;

    public BearerAuthFilter(JwtService jwtService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtService = jwtService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = getBearerToken(request);
        if (token == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            var sub = jwtService.verifyToken(token);
            var authenticationToken = new UsernamePasswordAuthenticationToken(sub, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (InvalidBearerTokenException e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String getBearerToken(HttpServletRequest request) {
        var bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return bearerToken.replace(BEARER_PREFIX, "");
    }
}
