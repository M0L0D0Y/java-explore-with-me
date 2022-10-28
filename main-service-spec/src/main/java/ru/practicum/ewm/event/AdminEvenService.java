package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;

import java.util.List;

public interface AdminEvenService {
    List<Event> findEvents(List<Long> users, List<String> states, List<Long> categories,
                           String rangeStart, String rangeEnd, Integer from, Integer size);

    Event redactionEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    Event publishEvent(long eventId);

    Event rejectedEvent(long eventId);
}
