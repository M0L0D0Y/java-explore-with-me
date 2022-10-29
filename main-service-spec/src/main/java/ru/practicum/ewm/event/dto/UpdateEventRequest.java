package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.common.Update;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @Size(max = 25, message = "Превышен лимит символов")
    @NotBlank(groups = Update.class, message = "Нет даты и времени на которые намечено событие")
    private String eventDate;
    @NotNull(groups = Update.class, message = "Не указан id обновляемого события")
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    @Max(120)
    @NotBlank(groups = Update.class, message = "Не указан заголовок события")
    private String title;

}
