package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.ewm")
public class MainServiceSpec {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceSpec.class, args);
    }

}