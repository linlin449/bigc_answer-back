FROM openjdk:8
MAINTAINER th1nk
COPY target/*.jar /app/app.jar
COPY target/application.yml /app/
EXPOSE 8080 8081 8082 8083 8084
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.config.additional-location=/app/application.yml"]