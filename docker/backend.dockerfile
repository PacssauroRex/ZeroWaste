FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y openjdk-21-jdk maven

COPY ./backend/zerowaste .

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk

EXPOSE 8080

COPY --from=build /target/*.jar /app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]