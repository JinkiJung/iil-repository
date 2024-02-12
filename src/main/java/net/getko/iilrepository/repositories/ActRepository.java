package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Act;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActRepository extends JpaRepository<Act, UUID> {
}
