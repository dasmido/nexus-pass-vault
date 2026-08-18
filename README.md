# Nexus Pass Vault

Self-Hosted Password Manager, Inspired from Apple MacOS Paaswords application.


# Gallery
<img width="1427" height="647" alt="Screenshot 2026-08-17 at 23 26 24" src="https://github.com/user-attachments/assets/e7d13ca0-e0e0-4202-9b5b-3f11935b5fdd" />


## Modules

- `nexus-jpa`: JPA entity and repository.
- `nexus-rest`: REST API controller and DTOs.
- `nexus-svelte`: Svelte dashboard source and build automation.
- `nexus-app`: Spring Boot runnable app that serves API and frontend.


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


