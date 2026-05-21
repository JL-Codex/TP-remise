# TP Remise — Step: RemiseDao (Hibernate & Spring Data JPA)

This document describes the additions made to the billing project to implement two new DAO strategies for `Remise` persistence, alongside a REST API for transaction management.

---

## Context

The project already had a Spring JDBC-based `RemiseRepository` handling discount lookups. This step introduces:

- A **common DAO interface** (`RemiseDao`) to unify all implementations under a single contract.
- **`RemiseHibernateDao`** — a plain Hibernate (`SessionFactory`) implementation.
- **`RemiseSpringDataDao`** — a Spring Data JPA implementation backed by a `JpaRepository`.
- A **REST API** for transactions (create, read, update, delete).

---

## Project Structure (additions highlighted)

```
SPRING_RemiseDB/src/main/java/org/example/
│
├── dao/                             ← NEW package
│   ├── RemiseDao.java               ← NEW — common DAO interface
│   ├── RemiseJdbcDao.java           ← NEW — Spring JDBC implementation
│   ├── RemiseHibernateDao.java      ← NEW — Hibernate SessionFactory implementation
│   ├── RemiseJpaRepository.java     ← NEW — Spring Data JPA repository interface
│   └── RemiseSpringDataDao.java     ← NEW — Spring Data JPA implementation
│
├── model/
│   ├── Remise.java                  ← MODIFIED — added no-arg constructor + setters
│   ├── RemiseEntity.java            ← NEW — JPA @Entity for the REMISE table
│   └── Transaction.java
│
├── controller/
│   ├── RemiseController.java
│   └── TransactionController.java   ← REST API (create, read, delete)
│
├── service/
│   ├── IRemise.java
│   ├── RemiseTauxBDD.java
│   └── TransactionService.java
│
└── repository/
    ├── RemiseRepository.java        ← original Spring JDBC repository (unchanged)
    └── TransactionRepository.java
```

---

### 1. `RemiseDao` — Common Interface

```java
public interface RemiseDao {
    Optional<Remise> findByMontant(double montant);
    Remise save(Remise remise);
    void update(Remise remise);
    void deleteById(Long id);
}
```

---

### 2. `RemiseEntity` — JPA Entity

A `@Entity` class mapping the existing `REMISE` table, used exclusively by the JPA/Hibernate layer. It provides two bridge methods to avoid coupling the rest of the application to JPA annotations:

```java
public Remise toRemise()              // entity → plain POJO
public static RemiseEntity from(Remise) // plain POJO → entity
```

The existing plain `Remise` POJO is kept untouched for the JDBC layer.

---

### 3. `RemiseJdbcDao` — Spring JDBC Implementation

Replaces and formalises what `RemiseRepository` already did, now implementing `RemiseDao`. Key addition: `save()` returns the persisted `Remise` with its generated id via `GeneratedKeyHolder`.

---

### 4. `RemiseHibernateDao` — Hibernate Implementation

Uses Hibernate's `SessionFactory` (unwrapped from Spring's `EntityManagerFactory`) to execute HQL queries. Each operation opens its own `Session` and manages its transaction explicitly.

```java
// Example: finding a remise by amount range
session.createQuery(
    "FROM RemiseEntity r WHERE r.montantMin <= :m AND r.montantMax >= :m",
    RemiseEntity.class)
  .setParameter("m", montant)
  .uniqueResult();
```

---

### 5. `RemiseSpringDataDao` — Spring Data JPA Implementation

Declares a `JpaRepository` interface with a custom JPQL query:

```java
@Query("SELECT r FROM RemiseEntity r WHERE r.montantMin <= :montant AND r.montantMax >= :montant")
Optional<RemiseEntity> findByMontantRange(@Param("montant") double montant);
```

Spring generates the full implementation at runtime. `RemiseSpringDataDao` is a thin adapter that converts between `RemiseEntity` and `Remise`.

---

## Modified Files

### `pom.xml`

Added the Spring Data JPA starter, which transitively brings Hibernate:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### `application.properties`

Three JPA-related properties were added:

```properties
# Hibernate must NOT create/drop tables — schema.sql owns DDL
spring.jpa.hibernate.ddl-auto=none

# Ensure schema.sql runs before Hibernate tries to use the tables
spring.jpa.defer-datasource-initialization=true

# Hibernate dialect for H2
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

Without `ddl-auto=none`, Hibernate would attempt to create or validate the schema and conflict with `schema.sql`. Without `defer-datasource-initialization=true`, Hibernate could query tables that don't exist yet.

### `model/Remise.java`

Added a no-arg constructor and setters so `RemiseEntity.from(Remise)` can build an entity from a plain POJO.

---

## REST API — Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/transactions` | Create a transaction (discount calculated server-side) |
| `GET` | `/api/transactions` | List all transactions |
| `GET` | `/api/transactions/{id}` | Get a transaction by ID |
| `DELETE` | `/api/transactions/{id}` | Delete a transaction by ID |

### Example request

```bash
curl -X POST http://localhost:8080/api/transactions \
     -H "Content-Type: application/json" \
     -d '{"montant": 600}'
```

### Example response

```json
{
  "id": 1,
  "date": "2026-05-21T14:30:00",
  "montantAvant": 600.0,
  "montantApres": 480.0,
  "reduction": 120.0
}
```

---

## Architecture Overview

```
Controller
    │
    ▼
ITransactionService ◄──── TransactionService
                                │
                    ┌───────────┴──────────┐
                    ▼                      ▼
         TransactionRepository         IRemise
          (Spring JDBC)             (RemiseTauxBDD)
                                         │
                                         ▼
                                    RemiseDao
                              ┌─────────┼──────────┐
                              ▼         ▼           ▼
                        JdbcDao  HibernateDao  SpringDataDao
```

All consumers depend on interfaces (`RemiseDao`, `IRemise`, `ITransactionService`), never on concrete classes — consistent with the Dependency Inversion principle throughout the project.

---

## Running the Application

```bash
mvn spring-boot:run
```

The H2 console is available at `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:mem:remisedb`.

---

## Switching DAO Implementation

Each DAO bean is named (`remiseJdbcDao`, `remiseHibernateDao`, `remiseSpringDataDao`). To use a specific one, qualify the injection point:

```java
@Qualifier("remiseHibernateDao")
private RemiseDao remiseDao;
```

Or promote one as `@Primary` in a configuration class.
