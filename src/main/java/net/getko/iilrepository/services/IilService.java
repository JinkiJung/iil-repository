package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.ActionValidationException;
import net.getko.iilrepository.exceptions.ConditionValidationException;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.NoCorrespondingGoalException;
import net.getko.iilrepository.models.domain.Action;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.domain.IilState;
import net.getko.iilrepository.repositories.ConditionRepository;
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
    private ConditionRepository conditionRepository;
    @Autowired
    IilRepository iilRepository;

    @Autowired
    ActionService actionService;

    @Autowired
    ConditionService conditionService;

    @Autowired
    ActorService actorService;

    @Transactional(readOnly = true)
    public List<Iil> findAll() {
        log.debug("Request to get all iils");
        return this.iilRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iil findById(UUID id) throws ConfigDataNotFoundException {
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

    public void updateState(Iil iil, IilState state) {
        // it is only possible to change to ACTIVATED only if there is an actor assigned
        if (state != IilState.NOT_ACTIVATED && iil.getActor() == null) {
            throw new IllegalArgumentException("Cannot change state to ACTIVATED without an actor");
        }
        iil.setState(state);
        this.iilRepository.save(iil);
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

        if (iil.getActor() != null) {
            // if the actor doesn't exist throw illegalargumentexception
            Actor fetched = actorService.findById(iil.getActor().getId());
            if (fetched == null) {
                throw new IllegalArgumentException("No actor corresponding to the given ID from actor");
            } else {
                iil.setActor(fetched);
            }
        }

        if (iil.getAct() != null) {
            if (iil.getAct().getId() == null) {
                Action action = actionService.createWithNewId(iil.getAct());
                iil.setAct(action);
            } else {
                Action fetched = actionService.findById(iil.getAct().getId());
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
            } else {
                // fetch the condition corresponding to the id of activateIf from condition repository
                Condition fetched = conditionService.findById(iil.getActivateIf().getId());
                if (fetched == null) {
                    throw new IllegalArgumentException("No condition corresponding to the given ID from activateIf");
                } else {
                    iil.setActivateIf(fetched);
                }
            }
        }

        if (iil.getFinishIf() != null) {
            if (iil.getFinishIf().getId() == null) {
                Condition condition = conditionService.createWithNewId(iil.getFinishIf());
                iil.setFinishIf(condition);
            } else {
                Condition fetched = conditionService.findById(iil.getFinishIf().getId());
                if (fetched == null) {
                    throw new IllegalArgumentException("No condition corresponding to the given ID from finishIf");
                } else {
                    iil.setFinishIf(fetched);
                }
            }
        }

        if (iil.getAbortIf() != null) {
            if (iil.getAbortIf().getId() == null) {
                Condition condition = conditionService.createWithNewId(iil.getAbortIf());
                iil.setAbortIf(condition);
            } else {
                Condition fetched = conditionService.findById(iil.getAbortIf().getId());
                if (fetched == null) {
                    throw new IllegalArgumentException("No condition corresponding to the given ID from abortIf");
                } else {
                    iil.setAbortIf(fetched);
                }
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
