package org.example.dto;

import java.time.LocalDateTime;

/**
 * DTO for returning transaction details to the client.
 */
public class TransactionResponseDTO {
    private Long id;
    private LocalDateTime date;
    private double montantAvant;
    private double montantApres;
    private double reduction;

    public TransactionResponseDTO() {
    }

    public TransactionResponseDTO(Long id, LocalDateTime date,
                                   double montantAvant, double montantApres,
                                   double reduction) {
        this.id = id;
        this.date = date;
        this.montantAvant = montantAvant;
        this.montantApres = montantApres;
        this.reduction = reduction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getMontantAvant() {
        return montantAvant;
    }

    public void setMontantAvant(double montantAvant) {
        this.montantAvant = montantAvant;
    }

    public double getMontantApres() {
        return montantApres;
    }

    public void setMontantApres(double montantApres) {
        this.montantApres = montantApres;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    @Override
    public String toString() {
        return "TransactionResponseDTO{" +
                "id=" + id +
                ", date=" + date +
                ", montantAvant=" + montantAvant +
                ", montantApres=" + montantApres +
                ", reduction=" + reduction +
                '}';
    }
}
