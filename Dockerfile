FROM openjdk:17-alpine

# Set working directory
WORKDIR /p2

# Copy the built JAR file from Jenkins workspace into the Docker image
COPY build/libs/*.jar p2.jar

# Expose port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "p2.jar"]


ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=963258741
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/swe304