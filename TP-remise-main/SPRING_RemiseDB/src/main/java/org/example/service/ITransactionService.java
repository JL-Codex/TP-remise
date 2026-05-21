package org.example.service;

import java.util.List;
import java.util.Optional;

import org.example.model.Transaction;

/**
 * Abstraction for transaction operations.
 *
 * Applying Interface Segregation (I) and Dependency Inversion (D):
 * all consumers depend on this interface, never on the concrete class.
 */
public interface ITransactionService {

    /**
     * Validates the amount, calculates the discount, creates and persists
     * the transaction in one atomic operation.
     *
     * Throws RemiseException if montant <= 0.
     */
    Transaction createFromMontant(double montant);

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    void update(Long id, double newMontantApres);

    void deleteById(Long id);

    List<Transaction> findAll();
}
