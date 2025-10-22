FROM openjdk:21-slim 
WORKDIR /app 
COPY target/parent-website-0.0.1-SNAPSHOT.jar app.jar 
EXPOSE 8080 
