FROM maven:3.8.6-eclipse-temurin-8-alpine AS builder-java
ADD ./ /build/app/
WORKDIR /build/app
RUN mvn package -s /build/app/settings.xml dependency:resolve -Dmaven.test.skip=true

FROM openjdk:8
COPY --from=builder-java /build/app/target/*.jar /app/app.jar
COPY src/main/resources/application.yml /app/config/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.config.additional-location=/app/config/application.yml"]