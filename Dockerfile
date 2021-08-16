## build
FROM maven:3.8.1-openjdk-8 AS build
WORKDIR /build
COPY . /build/
RUN mvn clean package

## base
FROM openjdk:8-jre-alpine
WORKDIR /opt/api/
COPY --from=build /build/target/interest-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]