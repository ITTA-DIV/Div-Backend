FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 최종 이미지를 위한 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]