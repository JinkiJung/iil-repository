package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, UUID> {
}
