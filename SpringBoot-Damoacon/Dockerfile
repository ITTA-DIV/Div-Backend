FROM openjdk:17-jdk AS builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Multi-Stage Build의 두 번째 단계: 최종 이미지
FROM python:3.10
WORKDIR /app

# Java 애플리케이션 JAR 파일 복사
COPY --from=builder app.jar /app/app.jar

# 가상환경 생성
RUN python -m venv /app/venv

# 가상환경 활성화
ENV PATH="/app/venv/bin:$PATH"

# 필요한 Python 라이브러리 설치
COPY requirements.txt /app/
RUN pip install -r requirements.txt

# 최종 이미지에서의 실행 명령어
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]
