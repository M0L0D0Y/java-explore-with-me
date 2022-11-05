package ru.practicum.ewm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
