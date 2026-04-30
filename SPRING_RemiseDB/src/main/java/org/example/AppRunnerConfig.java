package org.example;

import java.time.LocalDateTime;

import org.example.model.Transaction;
import org.example.service.IRemise;
import org.example.service.TransactionService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application runner configuration that demonstrates the full flow:
 * 1. Perform discount calculation
 * 2. Create and save a Transaction
 * 3. Update the Transaction
 * 4. Delete the Transaction
 */
@Configuration
public class AppRunnerConfig {

    @Bean
    public ApplicationRunner demonstrateFlow(
            IRemise remiseService, 
            TransactionService transactionService) {
        return args -> {
            System.out.println("\n=== Starting Demo Flow ===\n");

            // Step 1: Perform discount calculation
            System.out.println("Step 1: Calculating discount for amount 600.0");
            double montantAvant = 600.0;
            double reduction = remiseService.calculer(montantAvant);
            double montantApres = montantAvant - reduction;
            System.out.println("  Montant before: " + montantAvant);
            System.out.println("  Discount: " + reduction);
            System.out.println("  Montant after: " + montantApres);

            // Step 2: Create and save a Transaction
            System.out.println("\nStep 2: Creating and saving a Transaction");
            Transaction transaction = new Transaction();
            transaction.setDate(LocalDateTime.now());
            transaction.setMontantAvant(montantAvant);
            transaction.setMontantApres(montantApres);
            transaction.setRemiseId(1L);
            
            Transaction savedTransaction = transactionService.save(transaction);
            System.out.println("  Saved: " + savedTransaction);
            Long transactionId = savedTransaction.getId();

            // Step 3: Retrieve and update the Transaction
            System.out.println("\nStep 3: Updating the Transaction");
            double newMontantApres = montantApres - 10; // Simulate an update
            transactionService.update(transactionId, newMontantApres);
            System.out.println("  New montant_apres: " + newMontantApres);

            // Step 4: Delete the Transaction
            System.out.println("\nStep 4: Deleting the Transaction");
            transactionService.deleteById(transactionId);
            System.out.println("  Transaction with ID " + transactionId + " deleted");

            System.out.println("\n=== Demo Flow Complete ===\n");
            System.out.println("The application is now running. Access the API at:");
            System.out.println("  - POST   http://localhost:8080/api/transactions (create)");
            System.out.println("  - GET    http://localhost:8080/api/transactions (list all)");
            System.out.println("  - GET    http://localhost:8080/api/transactions/{id} (get by id)");
            System.out.println("  - DELETE http://localhost:8080/api/transactions/{id} (delete)");
            System.out.println("  - GET    http://localhost:8080/h2-console (H2 Database Console)\n");
        };
    }
}
