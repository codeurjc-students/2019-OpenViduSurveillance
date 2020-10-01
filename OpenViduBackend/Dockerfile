# our base image
FROM openjdk:8-jdk-alpine

# copy files required for the app to run
COPY target/openViduBackend-0.0.1-SNAPSHOT.jar backend.jar

# tell the port number the container should expose
EXPOSE 8080

# run the application
CMD ["java", "-jar", "backend.jar"]