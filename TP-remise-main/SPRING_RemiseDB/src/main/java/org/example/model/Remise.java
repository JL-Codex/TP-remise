package org.example.model;

public class Remise {
    private Long id;
    private double montantMin;
    private double montantMax;
    private double taux;

    public Remise() {}

    public Remise(Long id, double montantMin, double montantMax, double taux) {
        this.id = id;
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.taux = taux;
    }

    public Long getId()              { return id; }
    public double getMontantMin()    { return montantMin; }
    public double getMontantMax()    { return montantMax; }
    public double getTaux()          { return taux; }

    public void setId(Long id)                   { this.id = id; }
    public void setMontantMin(double montantMin) { this.montantMin = montantMin; }
    public void setMontantMax(double montantMax) { this.montantMax = montantMax; }
    public void setTaux(double taux)             { this.taux = taux; }
}