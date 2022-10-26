package ru.practicum.ewm.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
