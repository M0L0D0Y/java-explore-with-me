package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.common.Update;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateEventRequest {
    @Min(20)
    @Max(2000)
    @NotBlank(groups = Update.class, message = "Нет краткого описания события")
    private String annotation;
    private Long category;
    @Max(7000)
    @Min(20)
    @NotBlank(groups = Update.class, message = "Нет полного описаня события")
    private String description;
    @NotBlank(groups = Update.class, message = "Нет даты и времени на которые намечено событие")
    private String eventDate;
    @NotNull(groups = Update.class, message = "Не указан id обновляемого события")
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    @Max(120)
    @Min(3)
    @NotBlank(groups = Update.class, message = "Не указан заголовок события")
    private String title;

}
