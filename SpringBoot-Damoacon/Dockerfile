FROM openjdk:17-jdk AS builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

FROM python:3.10
WORKDIR /app

# 가상환경 생성
RUN python -m venv /app/venv

# 가상환경 활성화
ENV PATH="/app/venv/bin:$PATH"

# 필요한 Python 라이브러리 설치
COPY requirements.txt /app/
RUN pip install -r requirements.txt

# 실행 명령어
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
