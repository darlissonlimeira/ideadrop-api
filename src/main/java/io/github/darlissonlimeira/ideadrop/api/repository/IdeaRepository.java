package io.github.darlissonlimeira.ideadrop.api.repository;


import io.github.darlissonlimeira.ideadrop.api.entity.IdeaEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdeaRepository extends JpaRepository<IdeaEntity, String> {

    List<IdeaEntity> findAllByOrderByCreatedAtDesc(Limit limit);
}
