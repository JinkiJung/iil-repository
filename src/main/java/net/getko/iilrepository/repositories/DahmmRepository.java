package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Dahmm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DahmmRepository extends JpaRepository<Dahmm, UUID> {
}
