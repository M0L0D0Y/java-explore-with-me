package ru.practicum.mainservice.event.dto;

import lombok.Data;
import ru.practicum.mainservice.common.Update;

import javax.validation.constraints.NotNull;

@Data
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    @NotNull(groups = Update.class, message = "Не указан id обновляемого события")
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    private String title;

}
