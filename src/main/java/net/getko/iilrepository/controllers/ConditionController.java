package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.models.domain.ConditionType;
import net.getko.iilrepository.models.dto.ConditionDto;
import net.getko.iilrepository.services.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/conditions")
@Slf4j
public class ConditionController {
    @Autowired
    ConditionService conditionService;

    @Autowired
    DomainDtoMapper<Condition, ConditionDto> conditionDomainToDtoMapper;

    @Autowired
    DomainDtoMapper<ConditionDto, Condition> conditionDtoToDomainMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConditionDto>> getConditions() {
        log.debug("REST request to get a list of conditions");
        final List<Condition> conditions = this.conditionService.findAll();
        return ResponseEntity.ok()
                .body(this.conditionDomainToDtoMapper.convertToList(conditions, ConditionDto.class));
    }

    // get conditions by type
    @GetMapping(value = "/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConditionDto>> getConditionsByType(@PathVariable String type) {
        log.debug("REST request to get a list of conditions by type");
        ConditionType conditionType;
        try {
            conditionType = ConditionType.valueOf(type.toUpperCase());
            final List<Condition> conditions = this.conditionService.findByType(conditionType);
            return ResponseEntity.ok()
                    .body(this.conditionDomainToDtoMapper.convertToList(conditions, ConditionDto.class));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // return bad request with text "given type is not valid" when type is not on ConditionType
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConditionDto> getCondition(@PathVariable UUID id) {
        log.debug("REST request to get an condition : {}", id);
        final Condition result = this.conditionService.findById(id);
        return ResponseEntity.ok()
                .body(this.conditionDomainToDtoMapper.convertTo(result, ConditionDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConditionDto> createCondition(@Valid @RequestBody ConditionDto conditionDto) throws JsonProcessingException {
        log.debug("REST request to get an condition : {}", conditionDto);
        Condition condition = this.conditionDtoToDomainMapper.convertTo(conditionDto, Condition.class);
        final Condition result = this.conditionService.create(condition);
        return ResponseEntity.created(null)
                .body(this.conditionDomainToDtoMapper.convertTo(result, ConditionDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCondition(@PathVariable UUID id) {
        log.debug("REST request to delete an condition : {}", id);
        if (this.conditionService.findById(id) == null) {
            return ResponseEntity.notFound()
                    .build();
        }
        this.conditionService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConditionDto> updateCondition(@PathVariable UUID id, @Valid @RequestBody ConditionDto conditionDto) {
        log.debug("REST request to get an condition : {}", conditionDto);
        conditionDto.setId(id);
        Condition condition = this.conditionDtoToDomainMapper.convertTo(conditionDto, Condition.class);
        final Condition result = this.conditionService.update(condition);
        return ResponseEntity.ok()
                .body(this.conditionDomainToDtoMapper.convertTo(result, ConditionDto.class));
    }
}
