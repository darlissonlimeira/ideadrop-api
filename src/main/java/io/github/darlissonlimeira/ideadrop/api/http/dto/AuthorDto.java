package io.github.darlissonlimeira.ideadrop.api.http.dto;

import io.github.darlissonlimeira.ideadrop.api.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDto {

    String name;

    String email;

    public static AuthorDto fromUserEntity(UserEntity entity) {
        var authorDto = new AuthorDto();
        authorDto.setName(entity.getName());
        authorDto.setEmail(entity.getEmail());
        return authorDto;
    }
}