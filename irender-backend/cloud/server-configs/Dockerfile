FROM openjdk:8
WORKDIR /app
COPY target/server-configs-0.0.1-SNAPSHOT.jar server-configs.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar","/app/server-configs.jar"]