FROM openjdk:8-jdk-slim

RUN apt-get update && apt-get install -y maven

WORKDIR /app