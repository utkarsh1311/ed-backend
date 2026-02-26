package com.utkarsh.ed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdApplication.class, args);
    }


}
