FROM openjdk:21
LABEL authors="haat3"

docker run --name backend-postgres -e POSTGRES_PASSWORD=password -e POSTGRES_USER=root -e POSTGRES_DB=jwt_security -p 5432:5432 -d postgres

ADD backend/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
