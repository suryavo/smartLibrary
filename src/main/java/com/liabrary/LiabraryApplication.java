package com.liabrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LiabraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiabraryApplication.class, args);
		
		System.out.println("Hello");
	}

}
