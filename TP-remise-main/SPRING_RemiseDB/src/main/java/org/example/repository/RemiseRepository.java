package org.example.repository;

import org.example.model.Remise;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository for Remise persistence operations.
 * 
 * SOLID compliance:
 *  S — single responsibility: only handles database operations for remises.
 *  D — depends on JdbcTemplate (Spring's abstraction).
 */
@Repository
public class RemiseRepository {

    private final JdbcTemplate jdbcTemplate;

    public RemiseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find a remise by amount range.
     * Returns the matching remise if found, or null if no match.
     */
    public Remise findByMontantRange(double montant) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, montant_min, montant_max, taux FROM REMISE WHERE montant_min <= ? AND montant_max >= ?",
                    (rs, rowNum) -> new Remise(
                            rs.getLong("id"),
                            rs.getDouble("montant_min"),
                            rs.getDouble("montant_max"),
                            rs.getDouble("taux")
                    ),
                    montant, montant
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Get the taux (rate) for a given amount.
     * Returns 0 if no remise applies to the given amount.
     */
    public double getTauxForMontant(double montant) {
        try {
            Double taux = jdbcTemplate.queryForObject(
                    "SELECT taux FROM REMISE WHERE montant_min <= ? AND montant_max >= ?",
                    Double.class,
                    montant, montant
            );
            return taux != null ? taux : 0;
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }
}
