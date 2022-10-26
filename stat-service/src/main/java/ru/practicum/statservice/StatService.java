package ru.practicum.statservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.statservice", "ru.practicum.transfer"})
public class StatService {
    public static void main(String[] args) {
        SpringApplication.run(StatService.class, args);
    }
}