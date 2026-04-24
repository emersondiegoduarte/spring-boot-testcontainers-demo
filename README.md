# Spring Boot Testcontainers Demo

A minimal Spring Boot project demonstrating integration testing with [Testcontainers](https://testcontainers.com/) and a real PostgreSQL database.

## What It Does

The project models a simple `Usuario` (user) entity persisted via Spring Data JPA to PostgreSQL. Its primary focus is showing how to write **true integration tests** — tests that run against a real, ephemeral PostgreSQL container rather than an in-memory database or manual setup.

Key features:
- Spring Data JPA with PostgreSQL
- Flyway for schema migrations
- Testcontainers (JUnit 5) for spinning up a disposable PostgreSQL 16 container per test run
- `@DynamicPropertySource` to wire the container's connection details into the Spring context at runtime

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 21+ |
| Maven | 3.8+ |
| Docker | Running locally (required by Testcontainers) |

> Testcontainers manages the PostgreSQL container automatically — no manual database setup needed to run tests.

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java       # Application entry point
│   │   ├── Usuario.java               # JPA entity
│   │   └── UsuarioRepository.java     # Spring Data repository
│   └── resources/
│       ├── application.yml            # Datasource config (localhost PostgreSQL)
│       └── db/migration/
│           └── V1__init.sql           # Flyway migration — creates `usuario` table
└── test/
    ├── java/com/example/demo/
    │   └── UsuarioRepositoryTest.java  # Integration test with Testcontainers
    └── resources/
        └── application-test.yml       # Test profile config (overridden at runtime)
```

## Running the Tests

```bash
mvn test
```

Testcontainers will pull and start a `postgres:16` Docker image automatically, apply the Flyway migration, run the tests, and tear the container down afterwards.

### What the test does

`UsuarioRepositoryTest` verifies that persisting a `Usuario` entity generates a database-assigned ID:

```java
@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testSave() {
        Usuario u = new Usuario();
        u.setNome("Diego");
        Usuario saved = repository.save(u);
        assertNotNull(saved.getId());
    }
}
```

## Running the Application

The app expects a PostgreSQL instance at `localhost:5432` with database `test` and default credentials (`postgres`/`postgres`). Start one with Docker:

```bash
docker run --rm -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=test -p 5432:5432 postgres:16
```

Then run:

```bash
mvn spring-boot:run
```

Flyway will apply the migration on startup and the application will be ready.

## Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 4.0.5 |
| Language | Java 21 |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Testing | JUnit 5 + Testcontainers 1.19.8 |
| Build | Maven |
