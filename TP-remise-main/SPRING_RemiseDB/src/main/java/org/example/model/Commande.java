package org.example.model;

import org.example.service.IRemise;

public class Commande {

    private double Montan_avant_remise;
    private IRemise Remise;

    public Commande(double _montant, IRemise _remise){

        this.Montan_avant_remise = _montant;
        this.Remise = _remise;

    }

    public double apply_remise(){
        return this.Montan_avant_remise - this.Remise.calculer(Montan_avant_remise);
    }

    public double getMontan_avant_remise() {
        return Montan_avant_remise;
    }
}
