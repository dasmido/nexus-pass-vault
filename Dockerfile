# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

ARG MAVEN_PROFILE=with-svelte

# Copy the full multi-module project so Maven can resolve inter-module dependencies.
COPY . .

# Build all modules, including Svelte assets, and produce the Spring Boot fat JAR.
RUN mvn -f /workspace/pom.xml clean package -P${MAVEN_PROFILE} -DskipTests

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/nexus-app/target/nexus-app-1.0.0-SNAPSHOT.jar /app/nexus-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/nexus-app.jar"]


