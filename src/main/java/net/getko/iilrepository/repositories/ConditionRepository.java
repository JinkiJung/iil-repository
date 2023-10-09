package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.models.domain.ConditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ConditionRepository extends JpaRepository<Condition, UUID> {
    // create findByType method returns list of Conditions with the given type
    /**
     * Find by type.
     *
     * @param type the type
     * @return the list
     */
    @Query("select distinct condition " +
            "from Condition condition " +
            "where condition.type = :type")
    List<Condition> findByType(@Param("type") ConditionType type);
}

