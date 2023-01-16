package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.dto.IilDto;
import net.getko.iilrepository.services.IilService;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/iils")
@Slf4j
public class IilController {

    @Autowired
    IilService iilService;

    @Autowired
    DomainDtoMapper<Iil, IilDto> iilDomainToDtoMapper;

    @Autowired
    DomainDtoMapper<IilDto, Iil> iilDtoToDomainMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IilDto>> getIils() {
        log.debug("REST request to get a list of iils");
        final List<Iil> iils = this.iilService.findAll();
        return ResponseEntity.ok()
                .body(this.iilDomainToDtoMapper.convertToList(iils, IilDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IilDto> getIil(@PathVariable UUID id) {
        log.debug("REST request to get an iil : {}", id);
        final Iil result = this.iilService.findOne(id);
        return ResponseEntity.ok()
                .body(this.iilDomainToDtoMapper.convertTo(result, IilDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IilDto> createIil(@Valid @RequestBody IilDto iilDto) throws JsonProcessingException {
        log.debug("REST request to get an iil : {}", iilDto);
        // proper conversion to json
        ObjectMapper objectMapper = new ObjectMapper();
        String helpData = objectMapper.writeValueAsString(iilDto.getHelp());
        Map helpMap = objectMapper.readValue(helpData, Map.class);
        iilDto.setHelp(helpMap);
        String aboutData = objectMapper.writeValueAsString(iilDto.getAbout());
        Map aboutMap = objectMapper.readValue(aboutData, Map.class);
        iilDto.setHelp(aboutMap);
        Iil iil = this.iilDtoToDomainMapper.convertTo(iilDto, Iil.class);
        final Iil result = this.iilService.create(iil);
        return ResponseEntity.ok()
                .body(this.iilDomainToDtoMapper.convertTo(result, IilDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteIil(@PathVariable UUID id) {
        log.debug("REST request to delete an iil : {}", id);
        this.iilService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IilDto> updateIil(@PathVariable UUID id, @Valid @RequestBody IilDto iilDto) {
        log.debug("REST request to get an iil : {}", iilDto);
        iilDto.setId(id);
        Iil iil = this.iilDtoToDomainMapper.convertTo(iilDto, Iil.class);
        final Iil result = this.iilService.save(iil);
        return ResponseEntity.ok()
                .body(this.iilDomainToDtoMapper.convertTo(result, IilDto.class));
    }
}
