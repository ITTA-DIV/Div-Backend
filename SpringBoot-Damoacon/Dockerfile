# 단일 스테이지 사용
FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

FROM python:3.10

# 작업 디렉토리 설정
WORKDIR /app

# 가상환경 생성
RUN python -m venv /app/venv

# 가상환경 활성화
ENV PATH="/app/venv/bin:$PATH"

# Python 의존성 복사 및 설치
COPY requirements.txt /app/
RUN pip install -r requirements.txt

# 최종 이미지를 위한 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
