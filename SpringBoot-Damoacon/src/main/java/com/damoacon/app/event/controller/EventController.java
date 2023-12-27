package com.damoacon.app.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    @GetMapping("/runPythonScript")
    public ResponseEntity<String> runPythonScript() {
        try {
            // Python 스크립트 경로
            String pythonScript = "SpringBoot-Damoacon/crawling/test.py";

            // Python 가상 환경 경로
            String virtualEnvPath = "/app/venv/bin/python3.10";

            // 파이썬 모듈들의 경로
            String pythonModulesPath = "/app/venv/lib/python3.10/site-packages";

            // 프로세스 빌더 생성
            ProcessBuilder processBuilder = new ProcessBuilder(virtualEnvPath, pythonScript);
            processBuilder.redirectErrorStream(true);

            // 필요한 경우 환경 변수 설정
            processBuilder.environment().put("PYTHONPATH", pythonModulesPath);

            // 프로세스 실행
            Process process = processBuilder.start();

            // 프로세스 출력을 읽어오기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder jsonResult = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }

            // 이후에는 jsonResult를 원하는 방식으로 처리
            String jsonString = jsonResult.toString();

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);
            System.out.println(jsonString);
            // 프로세스의 출력을 문자열로 반환
            return ResponseEntity.ok(jsonString);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Failed to execute Python script.\n" + Arrays.toString(e.getStackTrace()));
        }
    }
}