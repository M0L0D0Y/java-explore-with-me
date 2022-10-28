package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventFullDto {
    @Min(20)
    @Max(2000)
    @NotBlank(message = "Нет краткого описания события")
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    private Long confirmedRequests;
    @NotBlank(message = "Нет даты создания")
    @Size(max = 25, message = "Превышен лимит символов")
    private String createdOn;
    @NotBlank
    @Size(max = 7000, message = "Превышен лимит символов")
    private String description;
    @NotBlank(message = "Нет даты создания")
    @Size(max = 25, message = "Превышен лимит символов")
    private String eventDate;
    private Long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    @NotNull
    private Integer participantLimit;
    @NotBlank(message = "Нет даты публикации")
    @Size(max = 25, message = "Превышен лимит символов")
    private String publishedOn;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    private EventState state;
    @NotBlank(message = "Нет названия")
    @Size(max = 120, message = "Превышен лимит символов")
    private String title;
    @NotNull
    private Long views;
}
