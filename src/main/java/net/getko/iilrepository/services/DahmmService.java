package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Dahmm;
import net.getko.iilrepository.repositories.IilRepository;
import net.getko.iilrepository.repositories.DahmmRepository;
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
public class DahmmService {
    @Autowired
    DahmmRepository dahmmRepository;

    @Autowired
    IilRepository iilRepository;

    @Transactional(readOnly = true)
    public List<Dahmm> findAll() {
        log.debug("Request to get all dahmms");
        return this.dahmmRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Dahmm findOne(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get NextFlow : {}", id);
        return Optional.ofNullable(id).map(this.dahmmRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No dahmms found for the provided ID"));
    }

    @Transactional
    public Dahmm save(Dahmm dahmm) {
        if (!this.iilRepository.findById(dahmm.getIilFrom()).isPresent()) {
            throw new DataNotFoundException("From element does not exist");
        }

        if (!this.iilRepository.findById(dahmm.getIilTo()).isPresent()) {
            throw new DataNotFoundException("To element does not exist");
        }

        return this.dahmmRepository.save(dahmm);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete dahmm : {}", id);

        this.dahmmRepository.findById(id)
                .map(Dahmm::getId)
                .ifPresentOrElse(
                        this.dahmmRepository::deleteById,
                        () -> {throw new DataNotFoundException("No dahmm found for the provided ID");}
                );
    }
}
