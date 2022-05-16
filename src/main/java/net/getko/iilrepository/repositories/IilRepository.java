package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Iil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IilRepository extends JpaRepository<Iil, UUID> {

}
