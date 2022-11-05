package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.participationRequest.ParticipationRequest;

import java.util.List;

public interface EventService {
    Event addEvent(long userId, Event event);

    Event getEvent(long id);

    List<Event> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                          String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    List<Event> getEventsForUser(long userId, int from, int size);

    Event updateEventForUser(long userId, UpdateEventRequest updateEventRequest);

    Event getFullInfoEventForUser(long userId, long eventId);

    Event cancelEvent(long userId, long eventId);

    List<ParticipationRequest> getRequestsInfoInEvents(long userId, long eventId);

    ParticipationRequest confirmRequestInEvents(long userId, long eventId, long reqId);

    ParticipationRequest rejectRequestInEvents(long userId, long eventId, long reqId);
}
