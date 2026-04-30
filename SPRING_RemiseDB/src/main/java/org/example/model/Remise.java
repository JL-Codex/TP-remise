package org.example.model;

public class Remise {
    private Long id;
    private double montantMin;
    private double montantMax;
    private double taux;

    public Remise(Long id, double montantMin, double montantMax, double taux) {
        this.id = id;
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.taux = taux;
    }

    public Long getId() { return id; }
    public double getMontantMin() { return montantMin; }
    public double getMontantMax() { return montantMax; }
    public double getTaux() { return taux; }
}