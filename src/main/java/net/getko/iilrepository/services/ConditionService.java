package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.NoCorrespondingGoalException;
import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.repositories.ConditionRepository;
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
public class ConditionService {
    @Autowired
    ConditionRepository conditionRepository;

    @Transactional(readOnly = true)
    public List<Condition> findAll() {
        log.debug("Request to get all conditions");
        return this.conditionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Condition findOne(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get an condition : {}", id);
        return Optional.ofNullable(id).map(this.conditionRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No condition found for the provided ID"));
    }

    @Transactional
    public Condition createWithNewId(Condition condition) {
        if (condition.getId() == null) {
            condition.setId(UUID.randomUUID());
            return this.conditionRepository.save(condition);
        } else {
            throw new EntityExistsException("Entity already exists with ID");
        }
    }

    @Transactional
    public Condition create(Condition condition) {
        if (condition.getId() != null && this.conditionRepository.findById(condition.getId()).isPresent()) {
            throw new NoCorrespondingGoalException("Duplicate data detected");
        }
        // The save and return
        return this.conditionRepository.save(condition);
    }

    @Transactional
    public Condition update(Condition condition) {
        if (!this.conditionRepository.findById(condition.getId()).isPresent()) {
            throw new DataNotFoundException("Data with given ID has not been found");
        }
        // The save and return
        return this.conditionRepository.save(condition);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete condition : {}", id);

        this.conditionRepository.findById(id)
                .map(Condition::getId)
                .ifPresentOrElse(
                        this.conditionRepository::deleteById,
                        () -> {throw new DataNotFoundException("No condition found for the provided ID");}
                );
    }
}
