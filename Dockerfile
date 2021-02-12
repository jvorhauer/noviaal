FROM openjdk:8-jdk-alpine
LABEL maintainer="jurjen@vorhauer.nl"
VOLUME /noviaal
ADD target/noviaal-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
