FROM ubuntu:latest
LABEL authors="jcall"

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/account-0.0.1-SNAPSHOT.jar /app/account.jar

EXPOSE 7072

ENTRYPOINT ["java", "-jar", "account.jar"]