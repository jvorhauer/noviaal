FROM adoptopenjdk/openjdk11:alpine
LABEL maintainer="j.vorhauer@novi-education.nl"
VOLUME /noviaal
ADD target/noviaal-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
