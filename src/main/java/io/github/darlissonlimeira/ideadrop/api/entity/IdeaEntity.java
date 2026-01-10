package io.github.darlissonlimeira.ideadrop.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ideas")
public class IdeaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private List<String> tags = List.of();

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp()
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    public IdeaEntity(String title, String summary, String description, List<String> tags, UserEntity author) {
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.tags = tags;
        this.author = author;
    }
}
