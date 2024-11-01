FROM openjdk:17


COPY target/*.jar foyer.jar


ENTRYPOINT ["java", "-jar", "foyer.jar"]