package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.domain.UserGroup;
import net.getko.iilrepository.models.dto.ActorDto;
import net.getko.iilrepository.repositories.UserGroupRepository;
import net.getko.iilrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class ActorService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Transactional(readOnly = true)
    public List<Actor> findAll() {
        log.debug("Request to get all actors");
        // return merged two lists from findAll() of User and UserGroup repositories
        return Stream.concat(this.userRepository.findAll().stream(), this.userGroupRepository.findAll().stream())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Actor findById(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get actor : {}", id);
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return (Actor)userOptional.get();
        } else {
            Optional<UserGroup> userGroupOptional = this.userGroupRepository.findById(id);
            if (userGroupOptional.isPresent()) {
                return (Actor) userGroupOptional.get();
            } else {
                throw new DataNotFoundException("No actor found for the provided ID");
            }
        }
    }

    @Transactional
    public Actor save(Actor actor) {
        if (actor.isGroup()) {
            return (Actor)this.userGroupRepository.save(new UserGroup(actor));
        } else {
            return (Actor)this.userRepository.save(new User(actor));
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete actor : {}", id);

        this.userRepository.findById(id)
                .map(User::getId)
                .ifPresentOrElse(
                        userId -> {
                            this.userRepository.deleteById(userId);
                        },
                        () -> {
                            this.userGroupRepository.findById(id)
                                    .map(UserGroup::getId)
                                    .ifPresentOrElse(
                                            userGroupId -> {
                                                this.userGroupRepository.deleteById(userGroupId);
                                            },
                                            () -> { throw new DataNotFoundException("No actor found for the provided ID"); }
                                    );
                        }
                );
    }

    @Transactional
    public Actor update(Actor actor) {
        if (actor.isGroup()) {
            return this.userGroupRepository.save((UserGroup)actor);
        } else {
            return this.userRepository.save((User)actor);
        }
    }

    @Transactional
    public Actor addUsersToGroup(UUID groupId, List<UUID> actorIdList) throws DataNotFoundException {
        // add users in actorDtoList to the group using addUserToGroup(), allowing duplicate key exception, not DataNotFoundException
        UserGroup userGroup = this.userGroupRepository.findById(groupId)
                .orElseThrow(() -> new DataNotFoundException("No user group found for the provided ID"));
        actorIdList.forEach(userId -> {
            try {
                User user = this.userRepository.findById(userId)
                        .orElseThrow(() -> new DataNotFoundException("No user found for the provided ID"));
                if (!userGroup.getActorList().contains(user)) {
                    userGroup.addUser(user);
                }
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        });
        return this.update(userGroup);
    }

    @Transactional
    public Actor removeUsersFromGroup(UUID groupId, List<UUID> actorIdList) throws DataNotFoundException {
        // remove users in actorDtoList from the group using removeUserFromGroup(), allowing duplicate key exception, not DataNotFoundException
        UserGroup userGroup = this.userGroupRepository.findById(groupId)
                .orElseThrow(() -> new DataNotFoundException("No user group found for the provided ID"));
        List<User> users = new ArrayList<>();
        actorIdList.forEach(userId -> {
            try {
                User user = this.userRepository.findById(userId)
                        .orElseThrow(() -> new DataNotFoundException("No user found for the provided ID"));
                users.add(user);
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        });
        users.forEach(user -> {
            userGroup.removeUser(user);
        });
        return this.update(userGroup);
    }

    @Transactional
    public Actor addUserToGroup(UUID groupId, UUID userId) throws DataNotFoundException, DuplicateKeyException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("No user found for the provided ID"));
        UserGroup userGroup = this.userGroupRepository.findById(groupId)
                .orElseThrow(() -> new DataNotFoundException("No user group found for the provided ID"));
        if (!userGroup.getActorList().contains(user)) {
            userGroup.addUser(user);
            return this.save(userGroup);
        } else {
            throw new DuplicateKeyException("User already exists in the group");
        }
    }

    // remove user from group
    @Transactional
    public Actor removeUserFromGroup(UUID groupId, UUID userId) throws DataNotFoundException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("No user found for the provided ID"));
        UserGroup userGroup = this.userGroupRepository.findById(groupId)
                .orElseThrow(() -> new DataNotFoundException("No user group found for the provided ID"));
        userGroup.removeUser(user);
        return this.userGroupRepository.save(userGroup);
    }
}
