FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app.jar"]
