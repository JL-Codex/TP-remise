package org.example.service;

import org.example.exception.RemiseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RemiseTauxBDD implements IRemise {

    private final JdbcTemplate jdbcTemplate;

    public RemiseTauxBDD(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double calculer(double montant) {
        if (montant <= 0) {
            throw new RemiseException("Le montant doit être supérieur à 0");
        }

        try {
            Double taux = jdbcTemplate.queryForObject(
                    "SELECT taux FROM REMISE WHERE montant_min <= ? AND montant_max >= ?",
                    Double.class,
                    montant, montant
            );

            if (taux == null) {
                return 0;
            }

            return montant * (taux / 100);
        } catch (EmptyResultDataAccessException ex) {
            // No matching row -> no remise
            return 0;
        }
    }
}