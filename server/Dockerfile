FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:18-ea-8-jdk-slim
COPY --from=build /target/server-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","server.jar"]