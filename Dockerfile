FROM openjdk:21
LABEL authors="haat3"

ADD backend/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
