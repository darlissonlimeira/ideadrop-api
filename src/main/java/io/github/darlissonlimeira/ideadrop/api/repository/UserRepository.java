package io.github.darlissonlimeira.ideadrop.api.repository;


import io.github.darlissonlimeira.ideadrop.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
}
