FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/damoacon-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /damoacon.jar

ENTRYPOINT ["java","-jar","/damoacon.jar"]