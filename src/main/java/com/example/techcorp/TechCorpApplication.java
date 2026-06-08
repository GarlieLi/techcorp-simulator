package com.example.techcorp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point
 * for launching the web version of TechCorp Simulator.
 */

@SpringBootApplication
public class TechCorpApplication {

    public static void main(String[] args) {

        SpringApplication.run(
            TechCorpApplication.class,
            args
        );
    }
}