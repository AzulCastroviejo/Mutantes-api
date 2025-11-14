package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // La forma simple es solo esta línea:
        SpringApplication.run(Main.class, args);
    }
}