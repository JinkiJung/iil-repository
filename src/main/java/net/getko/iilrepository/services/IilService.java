package net.getko.iilrepository.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.exceptions.DuplicatedDataException;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.dto.IilDto;
import net.getko.iilrepository.repositories.IilRepository;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    IilRepository iilRepo;

    @Transactional(readOnly = true)
    public List<Iil> findAll() {
        log.debug("Request to get all iils");
        return this.iilRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Iil findOne(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get Instance : {}", id);
        return Optional.ofNullable(id).map(this.iilRepo::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No instance found for the provided ID"));
    }

    /**
     * Save an iil.
     *
     * @param iil  the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Iil save(Iil iil) {
        log.debug("Request to save Instance : {}", iil);
        if (iil.getId() != null && this.iilRepo.findById(iil.getId()).isPresent()) {
            throw new DuplicatedDataException("Duplicate data detected");
        }

        if (iil.getGoal() != null && !this.iilRepo.findById(iil.getGoal().getId()).isPresent()) {
            throw new IllegalArgumentException("No instance corresponding to goal");
        }

        // The save and return
        return this.iilRepo.save(iil);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete Instance : {}", id);

        this.iilRepo.findById(id)
                .map(Iil::getId)
                .ifPresentOrElse(
                        this.iilRepo::deleteById,
                        () -> {throw new DataNotFoundException("No instance found for the provided ID");}
                );
    }
}
