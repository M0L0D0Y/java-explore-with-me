package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventShortDto {
    private Long id;
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
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotBlank
    @Size(max = 120, message = "Превышен лимит символов")
    private String title;
    @NotNull
    private Long views;
}
