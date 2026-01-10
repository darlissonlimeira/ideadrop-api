package io.github.darlissonlimeira.ideadrop.api.http.controller;

import io.github.darlissonlimeira.ideadrop.api.entity.UserEntity;
import io.github.darlissonlimeira.ideadrop.api.http.dto.AuthenticationRequest;
import io.github.darlissonlimeira.ideadrop.api.http.dto.AuthenticationResponse;
import io.github.darlissonlimeira.ideadrop.api.http.dto.RegisterRequest;
import io.github.darlissonlimeira.ideadrop.api.repository.UserRepository;
import io.github.darlissonlimeira.ideadrop.api.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) throws JoseException {
        var user = new UserEntity(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return authenticationService.authenticateUser(new AuthenticationRequest(request.getEmail(), request.getPassword()), response);
    }

    @PostMapping("/login")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request, HttpServletResponse response) throws JoseException {
        return authenticationService.authenticateUser(request, response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        var expiredRefreshCookie = authenticationService.createExpiredRefreshCookie();
        response.addCookie(expiredRefreshCookie);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refresh(@CookieValue("refreshToken") String refreshToken) throws JoseException {
        return authenticationService.refreshAuthentication(refreshToken);
    }
}
