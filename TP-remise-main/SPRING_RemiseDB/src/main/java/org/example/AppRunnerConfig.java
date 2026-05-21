package org.example;

import org.example.model.Transaction;
import org.example.service.IRemise;
import org.example.service.ITransactionService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demonstrates the full flow at startup:
 *   create remise → calculate → save transaction → update → delete
 *
 * SOLID compliance:
 *  D — depends on IRemise and ITransactionService abstractions only.
 */
@Configuration
public class AppRunnerConfig {

    @Bean
    public ApplicationRunner demonstrateFlow(
            IRemise remiseService,
            ITransactionService transactionService) {

        return args -> {
            System.out.println("\n=== Starting Demo Flow ===\n");

            // Step 1: Create transaction (validation + discount + persist in one call)
            System.out.println("Step 1: Creating a transaction for amount 600.0");
            Transaction saved = transactionService.createFromMontant(600.0);
            System.out.println("  Saved: " + saved);
            Long id = saved.getId();

            // Step 2: Update
            System.out.println("\nStep 2: Updating the transaction");
            double newMontantApres = saved.getMontantApres() - 10;
            transactionService.update(id, newMontantApres);
            System.out.println("  New montant_apres: " + newMontantApres);

            // Step 3: Delete
            System.out.println("\nStep 3: Deleting the transaction");
            transactionService.deleteById(id);
            System.out.println("  Transaction with ID " + id + " deleted");

            System.out.println("\n=== Demo Flow Complete ===\n");
            System.out.println("API available at:");
            System.out.println("  POST   http://localhost:8080/api/transactions");
            System.out.println("  GET    http://localhost:8080/api/transactions");
            System.out.println("  GET    http://localhost:8080/api/transactions/{id}");
            System.out.println("  DELETE http://localhost:8080/api/transactions/{id}");
            System.out.println("  GET    http://localhost:8080/h2-console\n");
        };
    }
}
