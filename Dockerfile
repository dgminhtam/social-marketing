FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/social-marketing-0.0.1-SNAPSHOT.jar social-marketing-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "social-marketing-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
