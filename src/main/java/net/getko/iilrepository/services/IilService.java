package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.DuplicatedDataException;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.repositories.IilRepository;
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
    public Iil create(Iil iil) {
        if (iil.getId() != null && this.iilRepository.findById(iil.getId()).isPresent()) {
            throw new DuplicatedDataException("Duplicate data detected");
        }
        // The save and return
        return this.save(iil);
    }

    /**
     * Save an iil.
     *
     * @param iil  the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Iil save(Iil iil) {
        log.debug("Request to save iil : {}", iil);

        if (iil.getGoal() != null && !this.iilRepository.findById(iil.getGoal()).isPresent()) {
            throw new IllegalArgumentException("No iil corresponding to goal");
        }

        // The save and return
        return this.iilRepository.save(iil);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete iil : {}", id);

        this.iilRepository.findById(id)
                .map(Iil::getId)
                .ifPresentOrElse(
                        this.iilRepository::deleteById,
                        () -> {throw new DataNotFoundException("No iil found for the provided ID");}
                );
    }
}
