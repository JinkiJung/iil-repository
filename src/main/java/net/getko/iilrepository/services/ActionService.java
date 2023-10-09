package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.NoCorrespondingGoalException;
import net.getko.iilrepository.models.domain.Action;
import net.getko.iilrepository.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ActionService {

    @Autowired
    ActionRepository actionRepository;

    @Transactional(readOnly = true)
    public List<Action> findAll() {
        log.debug("Request to get all actions");
        return this.actionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Action findById(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get an action : {}", id);
        return Optional.ofNullable(id).map(this.actionRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No action found for the provided ID"));
    }

    @Transactional
    public Action createWithNewId(Action action) {
        if (action.getId() == null) {
            action.setId(UUID.randomUUID());
            return this.actionRepository.save(action);
        } else {
            throw new EntityExistsException("Entity already exists with ID");
        }
    }

    @Transactional
    public Action create(Action action) {
        if (action.getId() != null && this.actionRepository.findById(action.getId()).isPresent()) {
            throw new NoCorrespondingGoalException("Duplicate data detected");
        }
        // The save and return
        return this.actionRepository.save(action);
    }

    @Transactional
    public Action update(Action action) {
        if (!this.actionRepository.findById(action.getId()).isPresent()) {
            throw new DataNotFoundException("Data with given ID has not been found");
        }
        // The save and return
        return this.actionRepository.save(action);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete action : {}", id);

        this.actionRepository.findById(id)
                .map(Action::getId)
                .ifPresentOrElse(
                        this.actionRepository::deleteById,
                        () -> {throw new DataNotFoundException("No action found for the provided ID");}
                );
    }
}
