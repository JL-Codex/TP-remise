package org.example.dao;

import org.example.model.Remise;
import org.example.model.RemiseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA implementation of {@link RemiseDao}.
 *
 * Delegates all persistence to {@link RemiseJpaRepository}; this class
 * exists solely to adapt the Spring Data API to the common DAO contract
 * and to convert between {@link RemiseEntity} (JPA) and {@link Remise} (POJO).
 *
 * SOLID:
 *  S — only bridges RemiseJpaRepository ↔ RemiseDao contract.
 *  D — depends on the RemiseJpaRepository and RemiseDao abstractions.
 */
@Repository("remiseSpringDataDao")
public class RemiseSpringDataDao implements RemiseDao {

    private final RemiseJpaRepository jpaRepository;

    public RemiseSpringDataDao(RemiseJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Remise> findByMontant(double montant) {
        return jpaRepository.findByMontantRange(montant)
                            .map(RemiseEntity::toRemise);
    }

    @Override
    public Remise save(Remise remise) {
        RemiseEntity entity  = RemiseEntity.from(remise);
        RemiseEntity saved   = jpaRepository.save(entity);
        return saved.toRemise();
    }

    @Override
    public void update(Remise remise) {
        // JPA save() acts as merge when the entity already has an id.
        jpaRepository.save(RemiseEntity.from(remise));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
