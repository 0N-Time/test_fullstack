# Stage 1: Build the Spring Boot app
FROM openjdk:21-jdk-slim AS build

# Install Maven, CA certs, and update Java truststore
RUN apt-get update && \
    apt-get install -y maven ca-certificates wget unzip && \
    apt-get clean && \
    update-ca-certificates

# Set workdir
WORKDIR /app

# Copy pom and preload dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]
