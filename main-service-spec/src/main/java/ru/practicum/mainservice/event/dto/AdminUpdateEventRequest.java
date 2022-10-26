package ru.practicum.mainservice.event.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.mainservice.event.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminUpdateEventRequest extends UpdateEventRequest {

    private Location location;
}
