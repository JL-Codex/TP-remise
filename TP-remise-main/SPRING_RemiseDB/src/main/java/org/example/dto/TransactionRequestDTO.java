package org.example.dto;

/**
 * DTO for creating a transaction from a given amount.
 * The discount calculation is performed server-side by the service.
 */
public class TransactionRequestDTO {
    private double montant;

    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(double montant) {
        this.montant = montant;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "TransactionRequestDTO{" +
                "montant=" + montant +
                '}';
    }
}
