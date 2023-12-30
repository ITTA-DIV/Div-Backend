FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# 호스트의 파이썬 가상환경을 마운트할 app디렉토리 생성
RUN mkdir /app

# 최종 이미지를 위한 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]