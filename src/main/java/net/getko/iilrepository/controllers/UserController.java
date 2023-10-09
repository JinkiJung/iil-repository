package net.getko.iilrepository.controllers;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.dto.UserDto;
import net.getko.iilrepository.services.UserService;
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
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    DomainDtoMapper<User, UserDto> userDomainToDtoMapper;

    @Autowired
    DomainDtoMapper<UserDto, User> userDtoToDomainMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUsers() {
        log.debug("REST request to get a list of users");
        final List<User> users = this.userService.findAll();
        return ResponseEntity.ok()
                .body(this.userDomainToDtoMapper.convertToList(users, UserDto.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        log.debug("REST request to get a user : {}", id);
        final User result = this.userService.findById(id);
        return ResponseEntity.ok()
                .body(this.userDomainToDtoMapper.convertTo(result, UserDto.class));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.debug("REST request to get an iil : {}", userDto);
        User user = this.userDtoToDomainMapper.convertTo(userDto, User.class);
        final User result = this.userService.save(user);
        return ResponseEntity.ok()
                .body(this.userDomainToDtoMapper.convertTo(result, UserDto.class));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.debug("REST request to delete user : {}", id);
        this.userService.delete(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("successfully deleted - ",
                id.toString());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto userDto) {
        log.debug("REST request to get an iil : {}", userDto);
        userDto.setId(id);
        User user = this.userDtoToDomainMapper.convertTo(userDto, User.class);
        final User result = this.userService.save(user);
        return ResponseEntity.ok()
                .body(this.userDomainToDtoMapper.convertTo(result, UserDto.class));
    }
}
