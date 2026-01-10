package io.github.darlissonlimeira.ideadrop.api.config;

import io.github.darlissonlimeira.ideadrop.api.exception.UnauthorizedException;
import io.github.darlissonlimeira.ideadrop.api.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BearerAuthProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public BearerAuthProvider(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var email = authentication.getName();
        if (authentication.getCredentials() == null) {
            throw new UnauthorizedException();
        }
        var password = authentication.getCredentials().toString();
        var user = userRepository.findByEmail(email).orElseThrow(UnauthorizedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException();
        }
        var principal = new AuthPrincipal(user.getId(), user.getName(), user.getEmail());
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
