package com.moacon.da;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication  // 스프링부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정. 이 어노테이션이 있는 위치부터 설정을 읽어가기 떄문에 이 클래스는 항상 프로젝트 최상단에 위치해야함.
public class Application {
    public static void main(String[] args) {
        // 내장 WAS(Web Application Server) 실행. 내장 WAS란 외부에 WAS를 두지 않고 내부에서 WAS를 실행하는 것.
        // 항상 서버에 톰캣을 설치할 필요가 없고 스프링부트로 만들어진 Jar 파일로 실행하면 됨.
        SpringApplication.run(Application.class, args);
    }
}
