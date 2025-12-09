# ==== Development ====
FROM eclipse-temurin:21-jdk-alpine AS development
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvn", "spring-boot:run"]

# ==== Build ====
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# ==== Production ====
FROM eclipse-temurin:21-jre-alpine AS production
WORKDIR /app
RUN addgroup -g 1001 appgroup && adduser -D -u 1001 appuser -G appgroup
USER appuser
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]