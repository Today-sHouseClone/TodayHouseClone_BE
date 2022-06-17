package com.hanghae.Today.sHouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 시간 자동 변경이 가능하도록 합니다.
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
