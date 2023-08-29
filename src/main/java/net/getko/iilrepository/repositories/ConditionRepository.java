package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConditionRepository extends JpaRepository<Condition, UUID> {
}

