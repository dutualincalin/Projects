# Build
FROM maven:3.8.1-openjdk-17-slim AS build
COPY /src /home/app/src
COPY pom.xml home/app
RUN mvn -f /home/app/pom.xml clean package

# Package
FROM eclipse-temurin:17-jdk
COPY --from=build /home/app/target/Tema2SPRC-0.0.1-SNAPSHOT.jar /usr/local/lib/tema2sprc.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/usr/local/lib/tema2sprc.jar"]
