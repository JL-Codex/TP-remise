package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.model.Transaction;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository for Transaction persistence operations.
 * 
 * SOLID compliance:
 *  S — single responsibility: only handles database operations for transactions.
 *  D — depends on JdbcTemplate (Spring's abstraction), not raw JDBC.
 */
@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a new transaction and return it with generated ID.
     */
    public Transaction save(Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO TRANSACTION (date, montant_avant, montant_apres, remise_id) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setTimestamp(1, java.sql.Timestamp.valueOf(transaction.getDate()));
                ps.setDouble(2, transaction.getMontantAvant());
                ps.setDouble(3, transaction.getMontantApres());
                ps.setLong(4, transaction.getRemiseId());
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            transaction.setId(keyHolder.getKey().longValue());
        }

        System.out.println("Transaction saved: " + transaction);
        return transaction;
    }

    /**
     * Find a transaction by ID.
     */
    public Optional<Transaction> findById(Long id) {
        try {
            Transaction transaction = jdbcTemplate.queryForObject(
                    "SELECT * FROM TRANSACTION WHERE id = ?",
                    (rs, rowNum) -> new Transaction(
                            rs.getLong("id"),
                            rs.getTimestamp("date") != null ? rs.getTimestamp("date").toLocalDateTime() : null,
                            rs.getDouble("montant_avant"),
                            rs.getDouble("montant_apres"),
                            rs.getLong("remise_id")
                    ),
                    id
            );
            return Optional.of(transaction);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    /**
     * Update a transaction's montant_apres.
     */
    public void update(Long id, double newMontantApres) {
        jdbcTemplate.update(
                "UPDATE TRANSACTION SET montant_apres = ? WHERE id = ?",
                newMontantApres, id
        );
        System.out.println("Transaction updated with id: " + id);
    }

    /**
     * Delete a transaction by ID.
     */
    public void deleteById(Long id) {
        jdbcTemplate.update(
                "DELETE FROM TRANSACTION WHERE id = ?", id
        );
        System.out.println("Transaction deleted with id: " + id);
    }

    /**
     * Find all transactions.
     */
    public List<Transaction> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM TRANSACTION",
                (rs, rowNum) -> new Transaction(
                        rs.getLong("id"),
                        rs.getTimestamp("date") != null ? rs.getTimestamp("date").toLocalDateTime() : null,
                        rs.getDouble("montant_avant"),
                        rs.getDouble("montant_apres"),
                        rs.getLong("remise_id")
                )
        );
    }
}
