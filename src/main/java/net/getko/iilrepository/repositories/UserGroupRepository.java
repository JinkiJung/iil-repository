package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
}
