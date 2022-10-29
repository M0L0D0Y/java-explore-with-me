package ru.practicum.ewm.event;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
public class Location {
    @NotNull(message = "Не указана широта")
    private Float lat;
    @NotNull(message = "Не указана долгота")
    private Float lon;
}
