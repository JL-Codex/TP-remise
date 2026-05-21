package org.example.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.converter.TransactionConverter;
import org.example.dto.TransactionRequestDTO;
import org.example.dto.TransactionResponseDTO;
import org.example.exception.RemiseException;
import org.example.model.Transaction;
import org.example.service.ITransactionService;
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
 * SOLID compliance:
 *  S — thin controller: HTTP mapping and DTO conversion only; no business logic.
 *  D — depends on ITransactionService and TransactionConverter abstractions.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ITransactionService transactionService;
    private final TransactionConverter converter;

    public TransactionController(ITransactionService transactionService, TransactionConverter converter) {
        this.transactionService = transactionService;
        this.converter = converter;
    }

    /**
     * Create a new transaction from a raw amount.
     * Validation and discount calculation are delegated to the service.
     */
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO request) {

        Transaction saved = transactionService.createFromMontant(request.getMontant());
        return new ResponseEntity<>(converter.toDTO(saved), HttpStatus.CREATED);
    }

    /**
     * Retrieve a transaction by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);

        if (transaction.isPresent()) {
            return new ResponseEntity<>(converter.toDTO(transaction.get()), HttpStatus.OK);
        } else {
            throw new RemiseException("Transaction with ID " + id + " not found");
        }
    }

    /**
     * Delete a transaction by ID.
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
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        List<TransactionResponseDTO> responses = transactions.stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
