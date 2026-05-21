package org.example.dao;

import org.example.model.Remise;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

/**
 * Spring JDBC implementation of {@link RemiseDao}.
 *
 * This is the reference / baseline implementation that mirrors what
 * the original RemiseRepository already provided, now unified under
 * the common DAO contract.
 *
 * SOLID:
 *  S — only handles REMISE table SQL operations.
 *  D — depends on JdbcTemplate abstraction, not raw JDBC.
 */
@Repository("remiseJdbcDao")
public class RemiseJdbcDao implements RemiseDao {

    private final JdbcTemplate jdbcTemplate;

    public RemiseJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Remise> findByMontant(double montant) {
        try {
            Remise remise = jdbcTemplate.queryForObject(
                    "SELECT id, montant_min, montant_max, taux " +
                    "FROM REMISE WHERE montant_min <= ? AND montant_max >= ?",
                    (rs, rowNum) -> new Remise(
                            rs.getLong("id"),
                            rs.getDouble("montant_min"),
                            rs.getDouble("montant_max"),
                            rs.getDouble("taux")),
                    montant, montant);
            return Optional.ofNullable(remise);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Remise save(Remise remise) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO REMISE (montant_min, montant_max, taux) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, remise.getMontantMin());
            ps.setDouble(2, remise.getMontantMax());
            ps.setDouble(3, remise.getTaux());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey() != null
                ? keyHolder.getKey().longValue()
                : null;

        return new Remise(generatedId, remise.getMontantMin(),
                          remise.getMontantMax(), remise.getTaux());
    }

    @Override
    public void update(Remise remise) {
        jdbcTemplate.update(
                "UPDATE REMISE SET montant_min = ?, montant_max = ?, taux = ? WHERE id = ?",
                remise.getMontantMin(),
                remise.getMontantMax(),
                remise.getTaux(),
                remise.getId());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM REMISE WHERE id = ?", id);
    }
}
