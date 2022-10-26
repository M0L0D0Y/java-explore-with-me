package ru.practicum.mainservice.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
