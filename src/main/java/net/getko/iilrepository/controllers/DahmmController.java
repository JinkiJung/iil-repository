package net.getko.iilrepository.controllers;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Dahmm;
import net.getko.iilrepository.models.dto.DahmmDto;
import net.getko.iilrepository.services.DahmmService;
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
@RequestMapping("/api/dahmms")
@Slf4j
public class DahmmController {
    @Autowired
    DahmmService dahmmService;

    @Autowired
    DomainDtoMapper<Dahmm, DahmmDto> dahmmDomainToDtoManager;

    @Autowired
    DomainDtoMapper<DahmmDto, Dahmm> dahmmDtoToDomainManager;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DahmmDto>> getDahmms() {
        log.debug("REST request to get a list of dahmms");
        final List<Dahmm> dahmms = this.dahmmService.findAll();
        return ResponseEntity.ok()
                .body(this.dahmmDomainToDtoManager.convertToList(dahmms, DahmmDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DahmmDto> getDahmm(@PathVariable UUID id) {
        log.debug("REST request to get a dahmm: {}", id);
        final Dahmm result = this.dahmmService.findOne(id);
        return ResponseEntity.ok()
                .body(this.dahmmDomainToDtoManager.convertTo(result, DahmmDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DahmmDto> createDahmm(@Valid @RequestBody DahmmDto DahmmDto) {
        log.debug("REST request to get a dahmm: {}", DahmmDto);
        Dahmm dahmm = this.dahmmDtoToDomainManager.convertTo(DahmmDto, Dahmm.class);
        final Dahmm result = this.dahmmService.save(dahmm);
        return ResponseEntity.ok()
                .body(this.dahmmDomainToDtoManager.convertTo(result, DahmmDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDahmm(@PathVariable UUID id) {
        log.debug("REST request to delete a dahmm: {}", id);
        this.dahmmService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DahmmDto> updateDahmm(@PathVariable UUID id, @Valid @RequestBody DahmmDto DahmmDto) {
        log.debug("REST request to get a dahmm: {}", DahmmDto);
        DahmmDto.setId(id);
        Dahmm dahmm = this.dahmmDtoToDomainManager.convertTo(DahmmDto, Dahmm.class);
        final Dahmm result = this.dahmmService.save(dahmm);
        return ResponseEntity.ok()
                .body(this.dahmmDomainToDtoManager.convertTo(result, DahmmDto.class));
    }
}
