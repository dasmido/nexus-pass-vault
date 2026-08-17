# Nexus Pass Vault

Multi-module Maven project for a Password Vault SaaS demo.

## Modules

- `nexus-jpa`: JPA entity and repository.
- `nexus-rest`: REST API controller and DTOs.
- `nexus-svelte`: Svelte dashboard source and build automation.
- `nexus-app`: Spring Boot runnable app that serves API and frontend from the same host/port.


## Build

Standard backend build:

```bash
mvn clean test
```

Full build with Svelte compilation from the root POM:

```bash
mvn clean package -Pwith-svelte
```

Equivalent command using an absolute path:

```bash
mvn -f /Users/mohammedjamal/workspace/nexus-pass-vault/pom.xml clean package -Pwith-svelte
```

## Run

Run the Spring Boot app module:

```bash
mvn -pl nexus-app spring-boot:run
```

### Run with PostgreSQL

Set the PostgreSQL connection values, then start the application with the `postgres` Spring profile:

```bash
export NEXUS_DB_URL='jdbc:postgresql://db.dasmido.dev:5432/npvdb'
export NEXUS_DB_USERNAME='app_user'
export NEXUS_DB_PASSWORD='myPasswordA123'
mvn -pl nexus-app spring-boot:run -Dspring-boot.run.profiles=postgres

Run the packaged Spring Boot JAR directly:

```bash
java -jar /Users/mohammedjamal/workspace/nexus-pass-vault/nexus-app/target/nexus-app-1.0.0-SNAPSHOT.jar
```

Open:

- Dashboard: `http://localhost:8080/`
- API: `http://localhost:8080/api/passwords`


