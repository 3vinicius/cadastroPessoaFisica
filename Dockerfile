FROM openjdk:21-jdk-slim
LABEL authors="vinicius"


EXPOSE 8080
COPY ./target/cadastroApi-0.0.1-SNAPSHOT.jar ../app.jar
ENTRYPOINT ["java","-jar","../app.jar"]
