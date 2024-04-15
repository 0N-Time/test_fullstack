FROM openjdk:21
LABEL authors="haat3"

ADD backend/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profile.active=dev", "app.jar"]