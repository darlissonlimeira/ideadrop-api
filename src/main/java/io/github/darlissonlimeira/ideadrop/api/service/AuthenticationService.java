package io.github.darlissonlimeira.ideadrop.api.service;

import io.github.darlissonlimeira.ideadrop.api.config.AuthPrincipal;
import io.github.darlissonlimeira.ideadrop.api.http.dto.AuthenticationRequest;
import io.github.darlissonlimeira.ideadrop.api.http.dto.AuthenticationResponse;
import io.github.darlissonlimeira.ideadrop.api.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.jose4j.lang.JoseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request, HttpServletResponse response) throws JoseException {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        if (authPrincipal == null) {
            throw new BadCredentialsException("Authentication failed: AuthPrincipal is null");
        }

        var refreshCookie = createRefreshCookie(authPrincipal.getId());
        response.addCookie(refreshCookie);

        return AuthenticationResponse.builder()
                .id(authPrincipal.getId())
                .name(authPrincipal.getName())
                .email(authPrincipal.getEmail())
                .accessToken(jwtService.createAccessToken(authPrincipal.getId()))
                .build();
    }

    public AuthenticationResponse refreshAuthentication(String refreshToken) throws JoseException {
        var userId = jwtService.verifyToken(refreshToken);
        var user = userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("User Not Found Or Bad Credentials"));

        return AuthenticationResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(jwtService.createAccessToken(user.getId()))
                .build();
    }

    public Cookie createRefreshCookie(String userId) throws JoseException {
        var cookie = new Cookie("refreshToken", jwtService.createRefreshToken(userId));
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setPath("/");

        return cookie;
    }

    public Cookie createExpiredRefreshCookie() {
        var cookie = new Cookie("refreshToken", "");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setPath("/");

        return cookie;
    }
}
