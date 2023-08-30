package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.debug("Request to get all users");
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findOne(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get Instance : {}", id);
        return Optional.ofNullable(id).map(this.userRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No user found for the provided ID"));
    }

    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete Instance : {}", id);

        this.userRepository.findById(id)
                .map(User::getId)
                .ifPresentOrElse(
                        this.userRepository::deleteById,
                        () -> {throw new DataNotFoundException("No user found for the provided ID");}
                );
    }
}
