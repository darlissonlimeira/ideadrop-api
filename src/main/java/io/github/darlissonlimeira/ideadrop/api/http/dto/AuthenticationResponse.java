package io.github.darlissonlimeira.ideadrop.api.http.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationResponse {
    
    private String id;

    private String name;

    private String email;

    private String accessToken;
}
