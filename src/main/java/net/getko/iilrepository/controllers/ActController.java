package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Act;
import net.getko.iilrepository.models.dto.ActDto;
import net.getko.iilrepository.services.ActService;
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
@RequestMapping("/api/acts")
@Slf4j
public class ActController {
    @Autowired
    ActService actService;

    @Autowired
    DomainDtoMapper<Act, ActDto> actDomainToDtoMapper;

    @Autowired
    DomainDtoMapper<ActDto, Act> actDtoToDomainMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ActDto>> getActions() {
        log.debug("REST request to get a list of acts");
        final List<Act> acts = this.actService.findAll();
        return ResponseEntity.ok()
                .body(this.actDomainToDtoMapper.convertToList(acts, ActDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActDto> getAction(@PathVariable UUID id) {
        log.debug("REST request to get an action : {}", id);
        final Act result = this.actService.findById(id);
        return ResponseEntity.ok()
                .body(this.actDomainToDtoMapper.convertTo(result, ActDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActDto> createAction(@Valid @RequestBody ActDto actDto) throws JsonProcessingException {
        log.debug("REST request to get an act : {}", actDto);
        Act act = this.actDtoToDomainMapper.convertTo(actDto, Act.class);
        final Act result = this.actService.create(act);
        return ResponseEntity.ok()
                .body(this.actDomainToDtoMapper.convertTo(result, ActDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAction(@PathVariable UUID id) {
        log.debug("REST request to delete an action : {}", id);
        this.actService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActDto> updateAction(@PathVariable UUID id, @Valid @RequestBody ActDto actDto) {
        log.debug("REST request to get an act : {}", actDto);
        actDto.setId(id);
        Act act = this.actDtoToDomainMapper.convertTo(actDto, Act.class);
        final Act result = this.actService.update(act);
        return ResponseEntity.ok()
                .body(this.actDomainToDtoMapper.convertTo(result, ActDto.class));
    }
}
