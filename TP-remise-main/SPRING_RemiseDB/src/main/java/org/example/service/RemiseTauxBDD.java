package org.example.service;

import org.example.exception.RemiseException;
import org.example.repository.RemiseRepository;
import org.springframework.stereotype.Service;

/**
 * Remise strategy: lookup rate from database based on amount range.
 *
 * SOLID compliance:
 *  S — single responsibility: only database-driven discount calculation.
 *  D — depends on RemiseRepository abstraction.
 */
@Service
public class RemiseTauxBDD implements IRemise {

    private final RemiseRepository remiseRepository;

    public RemiseTauxBDD(RemiseRepository remiseRepository) {
        this.remiseRepository = remiseRepository;
    }

    @Override
    public double calculer(double montant) {
        if (montant <= 0) {
            throw new RemiseException("Le montant doit être supérieur à 0");
        }

        double taux = remiseRepository.getTauxForMontant(montant);
        return montant * (taux / 100);
    }
}