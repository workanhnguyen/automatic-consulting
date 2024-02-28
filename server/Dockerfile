FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM maven:3.8.4-openjdk-17-slim
COPY --from=build /target/server-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","server.jar"]