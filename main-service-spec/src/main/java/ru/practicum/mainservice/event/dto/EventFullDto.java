package ru.practicum.mainservice.event.dto;

import lombok.Data;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.event.Location;
import ru.practicum.mainservice.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    private Long confirmedRequests;
    @NotBlank
    private String createdOn;
    @NotBlank
    private String description;
    @NotBlank
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
    @NotBlank
    private String publishedOn;
    @NotNull
    private Boolean requestModeration;
    @NotBlank
    private String state;//Enum: [ PENDING, PUBLISHED, CANCELED ]
    @NotBlank
    private String title;
    @NotNull
    private Long views;
}
