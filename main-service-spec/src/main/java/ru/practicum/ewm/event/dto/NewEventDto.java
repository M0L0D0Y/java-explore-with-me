package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.common.Create;

import javax.validation.constraints.*;

@Data
public class NewEventDto {
    @Min(20)
    @Max(2000)
    @NotBlank(groups = Create.class, message = "Нет краткого описания события")
    private String annotation;
    @Positive
    @NotNull(groups = Create.class, message = "Нет категории события")
    private Long category;
    @Max(7000)
    @Min(20)
    @NotBlank(groups = Create.class, message = "Нет полного описаня события")
    private String description;
    @NotBlank(groups = Create.class, message = "Нет даты и времени на которые намечено событие")
    private String eventDate;
    @NotNull(groups = Create.class, message = "Не указана локация проведения события")
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Max(120)
    @Min(3)
    @NotBlank(groups = Create.class, message = "Не указан заголовок события")
    private String title;
}
