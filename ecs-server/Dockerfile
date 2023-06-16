FROM openjdk:17-jdk-alpine

WORKDIR /app

#COPY target/ecs-server-1.0-SNAPSHOT.jar app.jar

COPY target/ecs-server-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar


ENTRYPOINT ["java","-jar","app.jar"]
