# FROM openjdk:11
# LABEL maintainer="Vanja Teodorovic vanjateodorovic00@gmail.com"
# ADD target/BaksonLtd_project-1.0-SNAPSHOT.jar BaksonLtd_project.jar
# ENTRYPOINT ["java", "-jar", "BaksonLtd_project.jar"]

# Use an OpenJDK base image
FROM openjdk:11

# Set the working directory in the container
WORKDIR /app

# Copy the executable JAR file and any other necessary files from the target directory into the container
ADD target/BaksonLtd_project-1.0-SNAPSHOT.jar /app/BaksonLtd_project.jar

# Specify the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "BaksonLtd_project.jar"]
