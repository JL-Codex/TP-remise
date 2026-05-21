package org.example.model;

import jakarta.persistence.*;

/**
 * JPA entity mapping the REMISE table.
 * Used by RemiseHibernateDao and RemiseSpringDataDao.
 * The plain Remise POJO is kept for the Spring JDBC layer.
 */
@Entity
@Table(name = "REMISE")
public class RemiseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "montant_min", nullable = false)
    private double montantMin;

    @Column(name = "montant_max", nullable = false)
    private double montantMax;

    @Column(name = "taux", nullable = false)
    private double taux;

    /** Required by JPA. */
    public RemiseEntity() {}

    public RemiseEntity(double montantMin, double montantMax, double taux) {
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.taux       = taux;
    }

    public RemiseEntity(Long id, double montantMin, double montantMax, double taux) {
        this.id         = id;
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.taux       = taux;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }

    public double getMontantMin()                   { return montantMin; }
    public void   setMontantMin(double montantMin)  { this.montantMin = montantMin; }

    public double getMontantMax()                   { return montantMax; }
    public void   setMontantMax(double montantMax)  { this.montantMax = montantMax; }

    public double getTaux()               { return taux; }
    public void   setTaux(double taux)    { this.taux = taux; }

    /** Convenience: convert to the plain POJO used by the rest of the app. */
    public Remise toRemise() {
        return new Remise(id, montantMin, montantMax, taux);
    }

    /** Factory: build a RemiseEntity from the plain POJO. */
    public static RemiseEntity from(Remise remise) {
        return new RemiseEntity(remise.getId(), remise.getMontantMin(),
                                remise.getMontantMax(), remise.getTaux());
    }

    @Override
    public String toString() {
        return "RemiseEntity{id=" + id + ", montantMin=" + montantMin
                + ", montantMax=" + montantMax + ", taux=" + taux + "}";
    }
}
