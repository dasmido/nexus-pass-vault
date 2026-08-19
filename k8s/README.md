# K3s deployment

This folder contains a k3s-ready manifest set for the Spring Boot application
and its embedded Svelte frontend. Both are served by the same container on
port `8080`.

## What to change first

- `k8s/deployment.yaml`: currently points at `dasmod/nexus-pass-vault:latest`; pin a specific tag if you do not want automatic updates.
- `k8s/secret.yaml`: replace the placeholder PostgreSQL connection values with real values.
- `k8s/configmap.yaml`: update `MISC_CORS_ORIGIN` and any non-secret runtime settings.
- `k8s/ingress.yaml`: change `nexuspassvault.dasmido.dev` and the TLS settings if your DNS name or cert-manager issuer changes.

## Prerequisites on Ubuntu k3s

1. Install k3s with Traefik enabled, or install an ingress controller that can serve `networking.k8s.io/v1` Ingress objects.
2. Install cert-manager and make sure a `ClusterIssuer` named `letsencrypt-prod` already exists in the cluster.
3. Point your DNS record for `nexuspassvault.dasmido.dev` at the node or load balancer that fronts k3s.
4. Make sure port 80 and 443 are reachable from the internet so ACME HTTP-01 validation can complete.
5. Keep the Traefik `websecure` entrypoint enabled so the ingress can terminate HTTPS cleanly.

## Apply

```bash
kubectl apply -f ./k8s
```

If you prefer not to commit secrets, create them separately instead of using `k8s/secret.yaml`.

## Push the image to Docker Hub

Log in to Docker Hub. Use a personal access token when prompted for the password:

```bash
docker login
```

Build and push a single-architecture image when all k3s nodes use the same
architecture as your workstation:

```bash
docker build -t dasmod/nexus-pass-vault:latest .
docker push dasmod/nexus-pass-vault:latest
```

For mixed `amd64` and `arm64` k3s nodes, publish a multi-architecture image
instead:

```bash
docker buildx create --name nexus-builder --use 2>/dev/null || docker buildx use nexus-builder
docker buildx build \
	--platform linux/amd64,linux/arm64 \
	-t dasmod/nexus-pass-vault:latest \
	--push .
```

For production deployments, publish an immutable version tag as well as
`latest`, then set that version in `k8s/deployment.yaml`:

```bash
export IMAGE_TAG=1.0.0
docker buildx build \
	--platform linux/amd64,linux/arm64 \
	-t dasmod/nexus-pass-vault:${IMAGE_TAG} \
	-t dasmod/nexus-pass-vault:latest \
	--push .
```

Change the Deployment image to `dasmod/nexus-pass-vault:1.0.0`, then apply the
manifests and wait for the rollout:

```bash
kubectl apply -f ./k8s
kubectl rollout status deployment/nexus-pass-vault
```

## Runtime checks

- Container port: `8080`
- Readiness and liveness: TCP checks on port `8080` (the app does not include Spring Boot Actuator).
- Ingress TLS: handled by cert-manager and Let’s Encrypt production