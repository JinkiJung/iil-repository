package net.getko.iilrepository.services;

import lombok.extern.slf4j.Slf4j;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.domain.NextFlow;
import net.getko.iilrepository.repositories.IilRepository;
import net.getko.iilrepository.repositories.NextFlowRepository;
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
public class NextFlowService {
    @Autowired
    NextFlowRepository nextFlowRepository;

    @Autowired
    IilRepository iilRepository;

    @Transactional(readOnly = true)
    public List<NextFlow> findAll() {
        log.debug("Request to get all NextFlows");
        return this.nextFlowRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NextFlow findOne(UUID id) throws ConfigDataNotFoundException {
        log.debug("Request to get NextFlow : {}", id);
        return Optional.ofNullable(id).map(this.nextFlowRepository::findById).get()
                .orElseThrow(() -> new DataNotFoundException("No NextFlow found for the provided ID"));
    }

    @Transactional
    public NextFlow save(NextFlow nextFlow) {
        if (!this.iilRepository.findById(nextFlow.getFrom()).isPresent()) {
            throw new DataNotFoundException("From element does not exist");
        }

        if (!this.iilRepository.findById(nextFlow.getTo()).isPresent()) {
            throw new DataNotFoundException("To element does not exist");
        }

        return this.nextFlowRepository.save(nextFlow);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void delete(UUID id) throws DataNotFoundException {
        log.debug("Request to delete nextFlow : {}", id);

        this.nextFlowRepository.findById(id)
                .map(NextFlow::getId)
                .ifPresentOrElse(
                        this.nextFlowRepository::deleteById,
                        () -> {throw new DataNotFoundException("No NextFlow found for the provided ID");}
                );
    }
}
