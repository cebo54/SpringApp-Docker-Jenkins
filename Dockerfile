FROM openjdk:17-alpine
LABEL authors="cebo"
COPY p2/build/libs/p2.jar p2.jar
ENTRYPOINT ["java","-jar","p2.jar"]


ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=963258741
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/swe304