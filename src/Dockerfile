# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /verveapp

# Add the application's JAR file to the container
COPY target/verveapp.jar /app/verveapp.jar

# Run the application
CMD ["java", "-jar", "/app/verveapp.jar"]
