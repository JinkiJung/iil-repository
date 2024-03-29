package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
