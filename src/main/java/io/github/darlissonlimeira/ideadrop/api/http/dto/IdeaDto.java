package io.github.darlissonlimeira.ideadrop.api.http.dto;

import io.github.darlissonlimeira.ideadrop.api.entity.IdeaEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class IdeaDto implements Serializable {

    String id;

    String title;

    String summary;

    String description;

    List<String> tags;

    LocalDateTime createdAt;

    String author;

    public static IdeaDto fromEntity(IdeaEntity entity) {
        IdeaDto ideaDto = new IdeaDto();
        ideaDto.setId(entity.getId());
        ideaDto.setTitle(entity.getTitle());
        ideaDto.setSummary(entity.getSummary());
        ideaDto.setDescription(entity.getDescription());
        ideaDto.setTags(entity.getTags());
        ideaDto.setCreatedAt(entity.getCreatedAt());
        ideaDto.setAuthor(entity.getAuthor().getId());
        return ideaDto;
    }
}