# Starting another build stage with official openjdk base image
FROM maven:3.8.4-openjdk-17-slim AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY checkstyle.xml .
COPY src ./src
# Build the project and create the .jar
RUN mvn clean install -DskipTests
RUN java -Djarmode=layertools -jar /app/target/pet-bot.jar extract

FROM eclipse-temurin:17-jre
# Copy the built JAR file from the previous stage to the container
WORKDIR /app
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

