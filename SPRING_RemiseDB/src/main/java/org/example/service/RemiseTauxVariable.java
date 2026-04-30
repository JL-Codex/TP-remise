package org.example.service;

public class RemiseTauxVariable implements  IRemise{

    @Override
    public double calculer(double montant) {
            if (montant > 1000) {
                return montant * 0.02;
            } else {
                return 0;
            }
    }
}
