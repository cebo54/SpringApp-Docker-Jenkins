FROM openjdk:17-alpine AS builder
WORKDIR /p2
COPY --from=builder /p2/build/libs/*.jar p2.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "p2.jar"]



ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=963258741
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/swe304