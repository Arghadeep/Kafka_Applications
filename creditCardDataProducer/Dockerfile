# Use an official JDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot application jar file to the container
COPY build/libs/creditCardDataProducer-0.0.1-SNAPSHOT.jar creditCardDataProducer.jar

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "creditCardDataProducer.jar"]
