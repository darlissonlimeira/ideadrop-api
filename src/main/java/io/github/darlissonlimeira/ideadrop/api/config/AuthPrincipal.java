package io.github.darlissonlimeira.ideadrop.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthPrincipal {

    private String id;

    private String name;

    private String email;
}
