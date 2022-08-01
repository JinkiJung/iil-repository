package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.NextFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NextFlowRepository extends JpaRepository<NextFlow, UUID> {
}
