package org.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.exception.RemiseException;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

/**
 * Concrete implementation of ITransactionService.
 *
 * SOLID compliance:
 *  S — single responsibility: business rules for transactions only (persistence delegated to repository).
 *  D — depends on IRemise abstraction and TransactionRepository.
 */
@Service
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final IRemise remiseService;

    public TransactionService(TransactionRepository transactionRepository, IRemise remiseService) {
        this.transactionRepository = transactionRepository;
        this.remiseService = remiseService;
    }

    /**
     * Full creation flow: validate → calculate discount → persist.
     * Business rules belong here, not in the controller.
     */
    @Override
    public Transaction createFromMontant(double montant) {
        if (montant <= 0) {
            throw new RemiseException("Le montant doit être supérieur à 0");
        }

        double reduction    = remiseService.calculer(montant);
        double montantApres = montant - reduction;

        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setMontantAvant(montant);
        transaction.setMontantApres(montantApres);
        transaction.setRemiseId(1L);

        return save(transaction);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public void update(Long id, double newMontantApres) {
        transactionRepository.update(id, newMontantApres);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
