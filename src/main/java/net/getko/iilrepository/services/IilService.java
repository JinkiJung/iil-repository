package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.ActionValidationException;
import net.getko.iilrepository.exceptions.ConditionValidationException;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.NoCorrespondingGoalException;
import net.getko.iilrepository.models.domain.Action;
import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.repositories.IilRepository;
import net.getko.iilrepository.utils.ActionValidator;
import net.getko.iilrepository.utils.ConditionValidator;
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
public class IilService {
    @Autowired
    IilRepository iilRepository;

    @Autowired
    ActionService actionService;

    @Autowired
    ConditionService conditionService;

    @Transactional(readOnly = true)
    public List<Iil> findAll() {
        log.debug("Request to get all iils");
        return this.iilRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iil findOne(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get iil : {}", id);
        return Optional.ofNullable(id).map(this.iilRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No iil found for the provided ID"));
    }

    /**
     * Save an iil.
     *
     * @param iil  the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Iil create(Iil iil) throws ActionValidationException, ConditionValidationException {
        if (iil.getId() != null && this.iilRepository.findById(iil.getId()).isPresent()) {
            throw new NoCorrespondingGoalException("Duplicate data detected");
        }

        // The save and return
        return this.save(iil);
    }

    public void validateIil(Iil iil) throws ConditionValidationException, ActionValidationException {
        if (iil.getActivateIf() != null && !ConditionValidator.validate(iil.getActivateIf())) {
            throw new ConditionValidationException("ActivateIf has wrong format", null);
        }
        if (iil.getFinishIf() != null && !ConditionValidator.validate(iil.getFinishIf())) {
            throw new ConditionValidationException("FinishIf has wrong format", null);
        }
        if (iil.getAbortIf() != null && !ConditionValidator.validate(iil.getAbortIf())) {
            throw new ConditionValidationException("AbortIf has wrong format", null);
        }

        if (iil.getAct() != null && !ActionValidator.validate(iil.getAct())) {
            throw new ActionValidationException("Act has wrong format", null);
        }
    }

    /**
     * Save an iil.
     *
     * @param iil  the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Iil save(Iil iil) throws ActionValidationException, ConditionValidationException {
        log.debug("Request to save iil : {}", iil);

        // Try to find the instance if an ID is provided
        if(iil.getId() != null) {
            this.iilRepository.findById(iil.getId())
                    .ifPresentOrElse(existingInstance -> {}, () -> {
                        throw new DataNotFoundException("No instance found for the provided ID");
                    });
        }

        if (iil.getGoal() != null && !this.iilRepository.findById(iil.getGoal()).isPresent()) {
            throw new NoCorrespondingGoalException("No iil corresponding to goal");
        }

        if (iil.getAct() != null) {
            if (iil.getAct().getId() == null) {
                Action action = actionService.createWithNewId(iil.getAct());
                iil.setAct(action);
            } else {
                Action fetched = actionService.findOne(iil.getAct().getId());
                if (fetched == null) {
                    throw new IllegalArgumentException("No action corresponding to the given ID from act");
                } else {
                    iil.setAct(fetched);
                }
            }
        }

        if (iil.getActivateIf() != null) {
            if (iil.getActivateIf().getId() == null) {
                Condition condition = conditionService.createWithNewId(iil.getActivateIf());
                iil.setActivateIf(condition);
            }
        }

        if (iil.getFinishIf() != null) {
            if (iil.getFinishIf().getId() == null) {
                Condition condition = conditionService.createWithNewId(iil.getFinishIf());
                iil.setFinishIf(condition);
            }
        }

        this.validateIil(iil);

        // The save and return
        return this.iilRepository.save(iil);
    }

    @Transactional(propagation = Propagation.NESTED)
    public boolean delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete iil : {}", id);

        this.iilRepository.findById(id)
                .map(Iil::getId)
                .ifPresentOrElse(
                        this.iilRepository::deleteById,
                        () -> {throw new DataNotFoundException("No iil found for the provided ID");}
                );
        return true;
    }
}
