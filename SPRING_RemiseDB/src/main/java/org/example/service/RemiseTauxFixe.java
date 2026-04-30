package org.example.service;

public class RemiseTauxFixe implements IRemise {
    private double taux ;

    public RemiseTauxFixe(double _taux){
        this.taux = _taux;
    }
    @Override
    public double calculer(double montant) {
        return montant * this.taux;
    }

}
