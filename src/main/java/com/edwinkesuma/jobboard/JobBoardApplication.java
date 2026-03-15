package com.edwinkesuma.jobboard;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobBoardApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

        SpringApplication.run(JobBoardApplication.class, args);
    }

}
