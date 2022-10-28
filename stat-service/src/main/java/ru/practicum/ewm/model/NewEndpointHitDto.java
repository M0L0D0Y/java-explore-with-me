package ru.practicum.ewm.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewEndpointHitDto {
    @NotBlank(groups = Create.class, message = "Не указан app обновляемого события")
    private String app;
    @NotBlank(groups = Create.class, message = "Не указан uri обновляемого события")
    private String uri;
    @NotBlank(groups = Create.class, message = "Не указан ip обновляемого события")
    private String ip;
    @NotBlank(groups = Create.class, message = "Не указан timestamp обновляемого события")
    private String timestamp;
}
