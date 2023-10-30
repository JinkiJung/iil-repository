package net.getko.iilrepository.controllers;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.dto.ActorDto;
import net.getko.iilrepository.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/actors/group")
@Slf4j
public class UserGroupController {

    @Autowired
    ActorService actorService;

    @Autowired
    DomainDtoMapper<Actor, ActorDto> actorDomainToDtoMapper;

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActorDto> addUsersToGroup(@PathVariable UUID id, @RequestBody List<UUID> actorIdList) {
        log.debug("REST request to add an actor to a group");
        Actor result = this.actorService.addUsersToGroup(id, actorIdList);
        return ResponseEntity.ok()
                .body(this.actorDomainToDtoMapper.convertTo(result, ActorDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActorDto> removeUsersFromGroup(@PathVariable UUID id, @RequestBody List<UUID> actorIdList) {
        log.debug("REST request to remove an actor from a group");
        Actor result = this.actorService.removeUsersFromGroup(id, actorIdList);
        return ResponseEntity.ok()
                .body(this.actorDomainToDtoMapper.convertTo(result, ActorDto.class));
    }
}
