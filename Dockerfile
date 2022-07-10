FROM openjdk:8
MAINTAINER th1nk
COPY target/*.jar /app/app.jar
COPY target/application.yml /app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.config.additional-location=/app/application.yml"]