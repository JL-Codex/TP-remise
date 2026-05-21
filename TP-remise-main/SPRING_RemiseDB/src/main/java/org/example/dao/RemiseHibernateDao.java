package org.example.dao;

import jakarta.persistence.EntityManagerFactory;
import org.example.model.Remise;
import org.example.model.RemiseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Hibernate implementation of {@link RemiseDao}.
 *
 * Uses the Hibernate {@link SessionFactory} (obtained from the JPA
 * {@link EntityManagerFactory}) to execute HQL queries and manage
 * entity state directly.
 *
 * SOLID:
 *  S — only handles REMISE persistence via Hibernate.
 *  D — depends on SessionFactory abstraction; RemiseEntity bridges the JPA model.
 */
@Repository("remiseHibernateDao")
public class RemiseHibernateDao implements RemiseDao {

    private final SessionFactory sessionFactory;

    /**
     * Spring Boot auto-configures a JPA EntityManagerFactory; Hibernate's
     * SessionFactory is its underlying implementation and can be unwrapped.
     */
    public RemiseHibernateDao(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    // ── RemiseDao ────────────────────────────────────────────────────────────

    @Override
    public Optional<Remise> findByMontant(double montant) {
        try (Session session = sessionFactory.openSession()) {
            RemiseEntity entity = session
                    .createQuery(
                            "FROM RemiseEntity r " +
                            "WHERE r.montantMin <= :m AND r.montantMax >= :m",
                            RemiseEntity.class)
                    .setParameter("m", montant)
                    .uniqueResult();
            return Optional.ofNullable(entity).map(RemiseEntity::toRemise);
        }
    }

    @Override
    public Remise save(Remise remise) {
        RemiseEntity entity = RemiseEntity.from(remise);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
        return entity.toRemise();
    }

    @Override
    public void update(Remise remise) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            RemiseEntity entity = session.get(RemiseEntity.class, remise.getId());
            if (entity != null) {
                entity.setMontantMin(remise.getMontantMin());
                entity.setMontantMax(remise.getMontantMax());
                entity.setTaux(remise.getTaux());
                session.merge(entity);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            RemiseEntity entity = session.get(RemiseEntity.class, id);
            if (entity != null) {
                session.remove(entity);
            }
            session.getTransaction().commit();
        }
    }
}
