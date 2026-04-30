package org.example.model;

import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private LocalDateTime date;
    private double montantAvant;
    private double montantApres;
    private Long remiseId;

    // Default constructor
    public Transaction() {
    }

    public Transaction(Long id, LocalDateTime date,
                       double montantAvant, double montantApres,
                       Long remiseId) {
        this.id = id;
        this.date = date;
        this.montantAvant = montantAvant;
        this.montantApres = montantApres;
        this.remiseId = remiseId;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public double getMontantAvant() { return montantAvant; }
    public double getMontantApres() { return montantApres; }
    public Long getRemiseId() { return remiseId; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setMontantAvant(double montantAvant) { this.montantAvant = montantAvant; }
    public void setMontantApres(double montantApres) { this.montantApres = montantApres; }
    public void setRemiseId(Long remiseId) { this.remiseId = remiseId; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", montantAvant=" + montantAvant +
                ", montantApres=" + montantApres +
                ", remiseId=" + remiseId +
                '}';
    }
}
