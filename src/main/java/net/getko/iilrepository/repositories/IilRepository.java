package net.getko.iilrepository.repositories;

import net.getko.iilrepository.models.domain.Iil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IilRepository extends JpaRepository<Iil, UUID> {

    /**
     * Find iil list by actor
     *
     * @param actorId
     * @return iil list
     */
    @Query("select distinct iil from Iil iil where iil.actor.id = :actorId")
    List<Iil> findByActorId(@Param("actorId") UUID actorId);

}
