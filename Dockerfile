FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /build

# Copy both projects
COPY events/ events/
COPY wallet_core/ wallet_core/

# Build events first
WORKDIR /build/events
RUN chmod +x mvnw && \
    ./mvnw clean install -DskipTests

# Build wallet_core
WORKDIR /build/wallet_core
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

FROM eclipse-temurin:21-jre-jammy AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

WORKDIR /app
COPY --from=builder /build/wallet_core/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"] 