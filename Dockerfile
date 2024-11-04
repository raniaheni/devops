FROM alpine
RUN apk add openjdk17
EXPOSE 8080
# Define environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \JAVA_OPTS=""
# Set the working directory to /app
WORKDIR /app
# Copy the executable into the container at /app
ADD target/*.jar app.jar
# Run app.jar when the container launches
CMD ["java", "-jar", "/app/app.jar"] 
