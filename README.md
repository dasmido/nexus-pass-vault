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


