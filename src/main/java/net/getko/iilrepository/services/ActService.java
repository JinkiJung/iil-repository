package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.NoCorrespondingGoalException;
import net.getko.iilrepository.models.domain.Act;
import net.getko.iilrepository.repositories.ActRepository;
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
public class ActService {

    @Autowired
    ActRepository actRepository;

    @Transactional(readOnly = true)
    public List<Act> findAll() {
        log.debug("Request to get all actions");
        return this.actRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Act findById(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get an action : {}", id);
        return Optional.ofNullable(id).map(this.actRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No action found for the provided ID"));
    }

    @Transactional
    public Act createWithNewId(Act act) {
        if (act.getId() == null) {
            act.setId(UUID.randomUUID());
            return this.actRepository.save(act);
        } else {
            throw new EntityExistsException("Entity already exists with ID");
        }
    }

    @Transactional
    public Act create(Act act) {
        if (act.getId() != null && this.actRepository.findById(act.getId()).isPresent()) {
            throw new NoCorrespondingGoalException("Duplicate data detected");
        }
        // The save and return
        return this.actRepository.save(act);
    }

    @Transactional
    public Act update(Act act) {
        if (!this.actRepository.findById(act.getId()).isPresent()) {
            throw new DataNotFoundException("Data with given ID has not been found");
        }
        // The save and return
        return this.actRepository.save(act);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete action : {}", id);

        this.actRepository.findById(id)
                .map(Act::getId)
                .ifPresentOrElse(
                        this.actRepository::deleteById,
                        () -> {throw new DataNotFoundException("No action found for the provided ID");}
                );
    }
}
