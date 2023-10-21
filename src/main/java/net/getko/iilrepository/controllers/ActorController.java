package net.getko.iilrepository.controllers;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.dto.ActorDto;
import net.getko.iilrepository.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/actor")
@Slf4j
public class ActorController {

    @Autowired
    ActorService actorService;

    @Autowired
    DomainDtoMapper<ActorDto, Actor> actorDtoToDomainMapper;

    @Autowired
    DomainDtoMapper<Actor, ActorDto> actorDomainToDtoMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ActorDto>> getAllActors() {
        log.debug("REST request to get a list of actors");
        final List<Actor> actors = this.actorService.findAll();
        return ResponseEntity.ok()
                .body(this.actorDomainToDtoMapper.convertToList(actors, ActorDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActorDto> getActor(@PathVariable UUID id) {
        log.debug("REST request to get an actor : {}", id);
        final Actor result = this.actorService.findById(id);
        return ResponseEntity.ok()
                .body(this.actorDomainToDtoMapper.convertTo(result, ActorDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActorDto> createActor(@Valid @RequestBody ActorDto actorDto) {
        log.debug("REST request to get an actor : {}", actorDto);
        Actor actor = this.actorDtoToDomainMapper.convertTo(actorDto, Actor.class);
        final Actor result = this.actorService.save(actor);
        return ResponseEntity.ok()
                .body(this.actorDomainToDtoMapper.convertTo(result, ActorDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteActor(@PathVariable UUID id) {
        log.debug("REST request to delete actor : {}", id);
        this.actorService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActorDto> updateActor(@PathVariable UUID id, @Valid @RequestBody ActorDto actorDto) {
        log.debug("REST request to get an actor : {}", actorDto);
        actorDto.setId(id);
        Actor actor = this.actorDtoToDomainMapper.convertTo(actorDto, Actor.class);
        final Actor result = this.actorService.save(actor);
        return ResponseEntity.ok()
                .body(this.actorDomainToDtoMapper.convertTo(result, ActorDto.class));
    }
}
