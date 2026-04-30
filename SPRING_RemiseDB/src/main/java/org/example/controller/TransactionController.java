package org.example.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.dto.TransactionRequestDTO;
import org.example.dto.TransactionResponseDTO;
import org.example.exception.RemiseException;
import org.example.model.Transaction;
import org.example.service.IRemise;
import org.example.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for transaction management.
 * 
 * The API accepts only the amount (montant) and automatically computes the discount.
 * Discount calculation is performed server-side using the RemiseService.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final IRemise remiseService;

    public TransactionController(TransactionService transactionService, IRemise remiseService) {
        this.transactionService = transactionService;
        this.remiseService = remiseService;
    }

    /**
     * Create a new transaction from an amount.
     * 
     * Validates the amount and computes the discount using RemiseService.
     * 
     * @param request DTO containing montant (amount)
     * @return Created transaction with calculated discount
     */
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO request) {
        
        // Validate amount
        if (request.getMontant() <= 0) {
            throw new RemiseException("Le montant doit être supérieur à 0");
        }

        // Calculate discount using RemiseService
        double montantAvant = request.getMontant();
        double reduction = remiseService.calculer(montantAvant);
        double montantApres = montantAvant - reduction;

        // Create transaction object
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setMontantAvant(montantAvant);
        transaction.setMontantApres(montantApres);
        transaction.setRemiseId(1L); // Default remise ID (could be made dynamic)

        // Save transaction using TransactionService
        Transaction savedTransaction = transactionService.save(transaction);

        // Convert to response DTO
        TransactionResponseDTO response = new TransactionResponseDTO(
                savedTransaction.getId(),
                savedTransaction.getDate(),
                savedTransaction.getMontantAvant(),
                savedTransaction.getMontantApres(),
                reduction
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieve a transaction by ID.
     * 
     * @param id Transaction ID
     * @return Transaction details
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);
        
        if (transaction.isPresent()) {
            Transaction t = transaction.get();
            double reduction = t.getMontantAvant() - t.getMontantApres();
            TransactionResponseDTO response = new TransactionResponseDTO(
                    t.getId(),
                    t.getDate(),
                    t.getMontantAvant(),
                    t.getMontantApres(),
                    reduction
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new RemiseException("Transaction with ID " + id + " not found");
        }
    }

    /**
     * Delete a transaction by ID.
     * 
     * @param id Transaction ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);
        
        if (transaction.isPresent()) {
            transactionService.deleteById(id);
            return new ResponseEntity<>("Transaction with ID " + id + " deleted successfully", HttpStatus.OK);
        } else {
            throw new RemiseException("Transaction with ID " + id + " not found");
        }
    }

    /**
     * Get all transactions.
     * 
     * @return List of all transactions
     */
    @GetMapping
    public ResponseEntity<java.util.List<TransactionResponseDTO>> getAllTransactions() {
        java.util.List<Transaction> transactions = transactionService.findAll();
        java.util.List<TransactionResponseDTO> responses = new java.util.ArrayList<>();

        for (Transaction t : transactions) {
            double reduction = t.getMontantAvant() - t.getMontantApres();
            responses.add(new TransactionResponseDTO(
                    t.getId(),
                    t.getDate(),
                    t.getMontantAvant(),
                    t.getMontantApres(),
                    reduction
            ));
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
