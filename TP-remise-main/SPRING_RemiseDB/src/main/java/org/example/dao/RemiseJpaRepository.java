package org.example.dao;

import org.example.model.RemiseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link RemiseEntity}.
 *
 * Spring generates the implementation at runtime — no boilerplate needed.
 * {@link RemiseSpringDataDao} wraps this interface to satisfy {@link RemiseDao}.
 */
public interface RemiseJpaRepository extends JpaRepository<RemiseEntity, Long> {

    /**
     * Find the remise whose range covers the given amount.
     * JPQL is used because no derived-query naming convention covers
     * a "between two columns" predicate cleanly.
     */
    @Query("SELECT r FROM RemiseEntity r " +
           "WHERE r.montantMin <= :montant AND r.montantMax >= :montant")
    Optional<RemiseEntity> findByMontantRange(@Param("montant") double montant);
}
