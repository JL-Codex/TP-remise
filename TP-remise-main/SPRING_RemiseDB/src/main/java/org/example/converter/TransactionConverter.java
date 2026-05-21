package org.example.converter;

import org.example.dto.TransactionResponseDTO;
import org.example.model.Transaction;
import org.springframework.stereotype.Component;

/**
 * Converter for Transaction DTO operations.
 * 
 * SOLID compliance:
 *  S — single responsibility: handles all Transaction ↔ DTO conversions.
 *  Removes conversion logic from controllers.
 */
@Component
public class TransactionConverter {

    /**
     * Convert a Transaction entity to a TransactionResponseDTO.
     * Calculates the reduction (discount amount) from the before/after amounts.
     */
    public TransactionResponseDTO toDTO(Transaction transaction) {
        double reduction = transaction.getMontantAvant() - transaction.getMontantApres();
        
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getDate(),
                transaction.getMontantAvant(),
                transaction.getMontantApres(),
                reduction
        );
    }
}
