# Start with a base image containing Java runtime
FROM gradle:8.8.0-jdk17 as build

# Set the current working directory inside the image
WORKDIR /app

# Copy gradle executable to the image
COPY gradlew .
COPY gradle gradle

# Copy the build and settings files
COPY build.gradle.kts .
COPY settings.gradle.kts .

# downloads all dependencies
RUN ./gradlew dependencies

# Copy your source code
COPY src src

# Build a jar
RUN ./gradlew bootJar
RUN cp /app/build/libs/playground-0.0.1-SNAPSHOT.jar /app/app.jar

# Start with a base image containing Java runtime
FROM openjdk:17

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Set application's JAR file
ARG JAR_FILE=app.jar

# Add the application's JAR file to the container
COPY --from=build /app/app.jar app.jar

# Set Spring profile
ENV SPRING_PROFILES_ACTIVE=prod

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
