# Use an OpenJDK base image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the generated JAR file to the container
COPY target/Foyer-0.0.1-SNAPSHOT.jar /app/DevOps_Project.jar

# Expose the application port
EXPOSE 8081


