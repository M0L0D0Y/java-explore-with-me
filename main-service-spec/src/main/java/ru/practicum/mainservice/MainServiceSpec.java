package ru.practicum.mainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.mainservice", "ru.practicum.transfer"})
public class MainServiceSpec {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceSpec.class, args);
    }

}