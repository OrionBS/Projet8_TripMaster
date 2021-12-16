FROM openjdk:8-alpine
ARG JAR_FILE=./build/libs/tourGuide-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","app.jar"]