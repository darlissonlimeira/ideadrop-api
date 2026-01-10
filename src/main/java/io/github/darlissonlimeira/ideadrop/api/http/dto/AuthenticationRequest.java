package io.github.darlissonlimeira.ideadrop.api.http.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationRequest {

    @Email
    private String email;

    @NotBlank
    private String password;
}
