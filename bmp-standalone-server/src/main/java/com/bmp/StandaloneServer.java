package com.bmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bmp"})
public class StandaloneServer {
    public static void main(String[] args) {
        SpringApplication.run(StandaloneServer.class, args);
    }
}
