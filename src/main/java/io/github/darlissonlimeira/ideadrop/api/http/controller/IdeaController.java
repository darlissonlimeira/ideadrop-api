package io.github.darlissonlimeira.ideadrop.api.http.controller;


import io.github.darlissonlimeira.ideadrop.api.exception.InvalidOwnerException;
import io.github.darlissonlimeira.ideadrop.api.exception.ResourceNotFoundException;
import io.github.darlissonlimeira.ideadrop.api.http.dto.IdeaDto;
import io.github.darlissonlimeira.ideadrop.api.http.dto.SaveIdeaRequest;
import io.github.darlissonlimeira.ideadrop.api.repository.IdeaRepository;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ideas")
public class IdeaController {

    private final IdeaRepository ideaRepository;

    public IdeaController(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    @Cacheable(cacheNames = "ideas")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IdeaDto> getIdeas(@RequestParam(defaultValue = "20") int limit) {
        return ideaRepository.findAllByOrderByCreatedAtDesc(Limit.of(limit)).stream().map(IdeaDto::fromEntity).toList();
    }

    @Cacheable(cacheNames = "ideas", key = "#id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public IdeaDto getIdea(@PathVariable String id) {
        return ideaRepository.findById(id).map(IdeaDto::fromEntity).orElseThrow(() -> new ResourceNotFoundException("Idea Not Found."));
    }

    @CachePut(cacheNames = "ideas", key = "#result.id")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdeaDto createIdea(@RequestBody @Valid SaveIdeaRequest request, Principal principal) {
        return IdeaDto.fromEntity(ideaRepository.save(request.toIdeaEntity(principal.getName())));
    }

    @CachePut(cacheNames = "ideas", key = "#result.id")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public IdeaDto updateIdea(@PathVariable String id, @RequestBody @Valid SaveIdeaRequest request, Principal principal) {
        var authenticatedUser = principal.getName();
        var idea = ideaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Idea Not Found."));

        if (!idea.getAuthor().getId().equals(authenticatedUser)) {
            throw new InvalidOwnerException();
        }

        idea.setTitle(request.getTitle());
        idea.setSummary(request.getSummary());
        idea.setDescription(request.getDescription());
        idea.setTags(request.getTags());

        ideaRepository.save(idea);

        return IdeaDto.fromEntity(idea);
    }

    @CacheEvict(cacheNames = "ideas", key = "#id")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteIdea(@PathVariable String id, Principal principal) {
        var idea = ideaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Idea Not Found."));
        var authenticatedUser = principal.getName();

        if (!idea.getAuthor().getId().equals(authenticatedUser)) {
            throw new InvalidOwnerException();
        }

        ideaRepository.deleteById(id);
    }
}
