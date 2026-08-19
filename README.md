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
```

mvn -pl nexus-app spring-boot:run -Dspring-boot.run.profiles=postgres

Run the packaged Spring Boot JAR directly:

```bash
java -jar /Users/mohammedjamal/workspace/nexus-pass-vault/nexus-app/target/nexus-app-1.0.0-SNAPSHOT.jar
```

Open:

- Dashboard: `http://localhost:8080/`
- API: `http://localhost:8080/api/passwords`

## Deploy to k3s

The Docker image serves both the Svelte frontend and Spring Boot API from port
`8080`. Kubernetes exposes that one container port through the Service on port
`80`; frontend and API requests therefore use the same host and do not need a
separate frontend Service.

### Prerequisites

- A k3s cluster with Traefik enabled (the default k3s ingress controller).
- A PostgreSQL database reachable from the cluster.
- `kubectl` configured for the cluster.
- `cert-manager` with a `ClusterIssuer` named `letsencrypt-prod` if HTTPS is
	required.

Before applying the manifests, update the database connection values in
`k8s/secret.yaml` and the public hostname in `k8s/configmap.yaml` and
`k8s/ingress.yaml`. Do not commit real credentials to source control.

Build and publish an image for the k3s node architecture, then pin that tag in
`k8s/deployment.yaml`:

```bash
docker buildx build --platform linux/amd64,linux/arm64 \
	-t dasmod/nexus-pass-vault:latest --push .
```

Apply the complete manifest set:

```bash
kubectl apply -f k8s/
kubectl rollout status deployment/nexus-pass-vault
kubectl get ingress,service,pods -l app.kubernetes.io/name=nexus-pass-vault
```

With DNS pointing at the k3s node and cert-manager configured, open the host
declared in `k8s/ingress.yaml`. Without an Ingress, the Service remains
cluster-internal (`ClusterIP`); use port forwarding for a local check:

```bash
kubectl port-forward service/nexus-pass-vault 8080:80
```

Then open `http://localhost:8080/` for the dashboard or
`http://localhost:8080/api/passwords` for the API.

## Contributions

Contributions, issues, and feature requests are welcome!
Feel free to check the [issues page](https://github.com/dasmido/nexus-pass-vault/issues) if you want to contribute.


## Support

If you find this project useful and want to support its development, consider buying me a coffee!

[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-FFDD00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://www.buymeacoffee.com/dasmido)
