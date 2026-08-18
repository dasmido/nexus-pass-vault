# Nexus Pass Vault

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Svelte](https://img.shields.io/badge/Svelte-v5-orange?logo=svelte)
![Java Version](https://img.shields.io/badge/Java-21+-blue?logo=openjdk)
![npm](https://img.shields.io/badge/npm-v10+-red?logo=npm)

Self-Hosted Password Manager, Inspired by the Apple macOS Passwords application.

## Features

- **Self-Hosted & Private**: Total control over your secrets without relying on third-party cloud services.
- **macOS-Inspired UI**: Clean, modern dashboard powered by Svelte.
- **Spring Boot Core**: Robust backend built with Java and JPA.
- **Flexible Database Support**: Support for PostgreSQL in production.
- **Unified Distribution**: Svelte frontend compiles directly into the Spring Boot executable JAR.
- **PIN-Protected Password Reveal**: Safely view stored passwords using an interactive master PIN authorization popup.
- **PDF & Excel Export**: Export vault data directly into formatted `.pdf` and `.xlsx` files.

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

## Contributions

Contributions, issues, and feature requests are welcome!

- **Mohammed Jamal** ([@mohammedjamal](https://github.com/mohammedjamal)) — *Creator & Lead Maintainer*

Feel free to check the [issues page](https://github.com/mohammedjamal/nexus-pass-vault/issues) if you want to contribute.


## Support

If you find this project useful and want to support its development, consider buying me a coffee!

[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-FFDD00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://www.buymeacoffee.com/dasmido)
