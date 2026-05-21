# Quick Key Points — TP RemiseDao

* Goal: support **multiple DAO strategies** without changing business logic.

* Added a common interface:

```java
RemiseDao
```

* 3 DAO implementations:

  * `RemiseJdbcDao` → Spring JDBC
  * `RemiseHibernateDao` → Hibernate
  * `RemiseSpringDataDao` → Spring Data JPA

* Main architecture idea:

```text
Service → Interface → Implementation
```

* Benefits:

  * loose coupling
  * easier maintenance
  * interchangeable persistence layers
  * follows Dependency Inversion (SOLID)

* `RemiseEntity` added for JPA/Hibernate mapping:

```java
@Entity
```

* `Remise` remains a plain POJO for business logic.

* Bridge methods:

```java
toRemise()
from(Remise)
```

* Hibernate DAO uses:

```java
SessionFactory
Session
Transaction
```

* Spring Data JPA uses:

```java
JpaRepository
```

which auto-generates CRUD operations.

* Added REST API for transactions:

  * POST create
  * GET all/by id
  * DELETE by id

* `pom.xml`:

```xml
spring-boot-starter-data-jpa
```

* `application.properties`:

```properties
ddl-auto=none
```

→ prevents Hibernate from recreating tables.

```properties
defer-datasource-initialization=true
```

→ ensures `schema.sql` runs first.

* Main concepts learned:

  * DAO Pattern
  * JDBC vs Hibernate vs JPA
  * Dependency Injection
  * REST API
  * Layered Architecture
  * SOLID principles
