package com.detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.detection.controller")
@ComponentScan("com.detection.controller.rest")
public class ThirdPartyDetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyDetectionApplication.class, args);
    }
}
