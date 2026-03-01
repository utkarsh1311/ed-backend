package com.utkarsh.ed;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Force the server to run in Indian Standard Time
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }
}
