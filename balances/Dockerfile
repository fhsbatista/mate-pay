FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /build
COPY . .

RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests

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

# Install wait-for-it
RUN apt-get update && apt-get install -y wget && \
    wget -O /usr/local/bin/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && \
    chmod +x /usr/local/bin/wait-for-it.sh

USER appuser

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "balances_db:3306", "--", "java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]