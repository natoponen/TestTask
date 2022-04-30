package com.example.test.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class demoApplication {
    public static void main(String[] args) {
        SpringApplication.run(demoApplication.class, args);
    }
}
