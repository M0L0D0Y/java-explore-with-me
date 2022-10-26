package ru.practicum.ewm.event.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.ewm.event.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminUpdateEventRequest extends UpdateEventRequest {

    private Location location;
}
