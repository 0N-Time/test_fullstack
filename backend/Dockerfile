# Use the official OpenJDK image with Java 21 to build the app
FROM openjdk:21-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and package the application to create the JAR file
COPY src ./src
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the app with Java 21
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/app.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Set the active Spring profile as an environment variable
ENV SPRING_PROFILES_ACTIVE=dev

# Run the JAR file
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/app.jar"]
