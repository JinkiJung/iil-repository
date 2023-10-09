package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Action;
import net.getko.iilrepository.models.dto.ActionDto;
import net.getko.iilrepository.services.ActionService;
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
@RequestMapping("/api/actions")
@Slf4j
public class ActionController {
    @Autowired
    ActionService actionService;

    @Autowired
    DomainDtoMapper<Action, ActionDto> actionDomainToDtoMapper;

    @Autowired
    DomainDtoMapper<ActionDto, Action> actionDtoToDomainMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ActionDto>> getActions() {
        log.debug("REST request to get a list of actions");
        final List<Action> actions = this.actionService.findAll();
        return ResponseEntity.ok()
                .body(this.actionDomainToDtoMapper.convertToList(actions, ActionDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActionDto> getAction(@PathVariable UUID id) {
        log.debug("REST request to get an action : {}", id);
        final Action result = this.actionService.findById(id);
        return ResponseEntity.ok()
                .body(this.actionDomainToDtoMapper.convertTo(result, ActionDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActionDto> createAction(@Valid @RequestBody ActionDto actionDto) throws JsonProcessingException {
        log.debug("REST request to get an action : {}", actionDto);
        Action action = this.actionDtoToDomainMapper.convertTo(actionDto, Action.class);
        final Action result = this.actionService.create(action);
        return ResponseEntity.ok()
                .body(this.actionDomainToDtoMapper.convertTo(result, ActionDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAction(@PathVariable UUID id) {
        log.debug("REST request to delete an action : {}", id);
        this.actionService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActionDto> updateAction(@PathVariable UUID id, @Valid @RequestBody ActionDto actionDto) {
        log.debug("REST request to get an action : {}", actionDto);
        actionDto.setId(id);
        Action action = this.actionDtoToDomainMapper.convertTo(actionDto, Action.class);
        final Action result = this.actionService.update(action);
        return ResponseEntity.ok()
                .body(this.actionDomainToDtoMapper.convertTo(result, ActionDto.class));
    }
}
