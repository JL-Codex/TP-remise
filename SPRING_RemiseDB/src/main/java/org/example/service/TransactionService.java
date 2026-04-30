package org.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.model.Transaction;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Service
public class TransactionService {

    private final JdbcTemplate jdbcTemplate;

    public TransactionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transaction save(Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        
        // Use GeneratedKeyHolder to retrieve the auto-generated ID
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
        
        // Set the generated ID on the transaction object
        if (keyHolder.getKey() != null) {
            transaction.setId(keyHolder.getKey().longValue());
        }
        
        System.out.println("Transaction saved: " + transaction);
        return transaction;
    }

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

    public void update(Long id, double newMontantApres) {
        jdbcTemplate.update(
                "UPDATE TRANSACTION SET montant_apres = ? WHERE id = ?",
                newMontantApres, id
        );
        System.out.println("Transaction updated with id: " + id);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(
                "DELETE FROM TRANSACTION WHERE id = ?", id
        );
        System.out.println("Transaction deleted with id: " + id);
    }

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
