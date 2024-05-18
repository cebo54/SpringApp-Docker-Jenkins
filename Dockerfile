# First stage: build the application
FROM openjdk:17-alpine AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Second stage: run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=963258741
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/swe304