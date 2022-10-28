package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.Location;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminUpdateEventRequest extends UpdateEventRequest {

    private Location location;
}
