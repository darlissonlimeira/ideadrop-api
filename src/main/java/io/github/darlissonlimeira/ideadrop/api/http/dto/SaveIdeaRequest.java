package io.github.darlissonlimeira.ideadrop.api.http.dto;

import io.github.darlissonlimeira.ideadrop.api.entity.IdeaEntity;
import io.github.darlissonlimeira.ideadrop.api.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveIdeaRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String summary;

    @NotBlank
    private String description;

    private List<String> tags;

    public IdeaEntity toIdeaEntity(String userId) {
        var idea = new IdeaEntity();
        idea.setAuthor(UserEntity.withId(userId));
        idea.setTitle(this.getTitle());
        idea.setSummary(this.getSummary());
        idea.setDescription(this.getDescription());
        idea.setTags(this.getTags());

        return idea;
    }
}
