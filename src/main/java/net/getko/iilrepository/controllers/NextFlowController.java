package net.getko.iilrepository.controllers;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.NextFlow;
import net.getko.iilrepository.models.dto.NextFlowDto;
import net.getko.iilrepository.services.NextFlowService;
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
@RequestMapping("/api/nextflows")
@Slf4j
public class NextFlowController {
    @Autowired
    NextFlowService nextFlowService;

    @Autowired
    DomainDtoMapper<NextFlow, NextFlowDto> nextFlowDomainToDtoManager;

    @Autowired
    DomainDtoMapper<NextFlowDto, NextFlow> nextFlowDtoToDomainManager;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NextFlowDto>> getNextFlows() {
        log.debug("REST request to get a list of nextflows");
        final List<NextFlow> nextFlows = this.nextFlowService.findAll();
        return ResponseEntity.ok()
                .body(this.nextFlowDomainToDtoManager.convertToList(nextFlows, NextFlowDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NextFlowDto> getNextFlow(@PathVariable UUID id) {
        log.debug("REST request to get a nextflow: {}", id);
        final NextFlow result = this.nextFlowService.findOne(id);
        return ResponseEntity.ok()
                .body(this.nextFlowDomainToDtoManager.convertTo(result, NextFlowDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NextFlowDto> createNextFlow(@Valid @RequestBody NextFlowDto NextFlowDto) {
        log.debug("REST request to get a nextflow: {}", NextFlowDto);
        NextFlow nextflow = this.nextFlowDtoToDomainManager.convertTo(NextFlowDto, NextFlow.class);
        final NextFlow result = this.nextFlowService.save(nextflow);
        return ResponseEntity.ok()
                .body(this.nextFlowDomainToDtoManager.convertTo(result, NextFlowDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteNextFlow(@PathVariable UUID id) {
        log.debug("REST request to delete a nextflow: {}", id);
        this.nextFlowService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NextFlowDto> updateNextFlow(@PathVariable UUID id, @Valid @RequestBody NextFlowDto NextFlowDto) {
        log.debug("REST request to get a nextflow: {}", NextFlowDto);
        NextFlowDto.setId(id);
        NextFlow nextflow = this.nextFlowDtoToDomainManager.convertTo(NextFlowDto, NextFlow.class);
        final NextFlow result = this.nextFlowService.save(nextflow);
        return ResponseEntity.ok()
                .body(this.nextFlowDomainToDtoManager.convertTo(result, NextFlowDto.class));
    }
}
