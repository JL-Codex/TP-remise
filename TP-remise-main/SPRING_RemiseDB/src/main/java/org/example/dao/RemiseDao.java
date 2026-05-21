package org.example.dao;

import org.example.model.Remise;

import java.util.Optional;

/**
 * Contract for Remise persistence operations.
 *
 * Three concrete implementations exist:
 *   • RemiseJdbcDao       — Spring JDBC (wraps the existing RemiseRepository)
 *   • RemiseHibernateDao  — plain Hibernate SessionFactory
 *   • RemiseSpringDataDao — Spring Data JPA (JpaRepository)
 *
 * SOLID:
 *  I — interface contains only what every DAO must provide.
 *  D — consumers depend on this abstraction, not on any implementation.
 */
public interface RemiseDao {

    /** Find a remise whose range covers the given amount. */
    Optional<Remise> findByMontant(double montant);

    /** Persist a new remise and return it with its generated id. */
    Remise save(Remise remise);

    /** Update an existing remise (matched by id). */
    void update(Remise remise);

    /** Remove a remise by its id. */
    void deleteById(Long id);
}
