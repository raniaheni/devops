FROM openjdk:17

WORKDIR /app

COPY target/Foyer-0.0.1.jar /app/DevOps_Project.jar

EXPOSE 8081

CMD ["java", "-jar", "Foyer-0.0.1.jar"]
