package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.participationRequest.ParticipationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    Event addEvent(long userId, Event event);

    Event getEvent(long id, HttpServletRequest request);

    List<Event> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                          String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                          HttpServletRequest request);

    List<Event> findEvents(List<Long> users, List<String> states, List<Long> categories,
                           String rangeStart, String rangeEnd, Integer from, Integer size);

    List<Event> getEventsForUser(long userId, int from, int size);

    Event updateEventForUser(long userId, UpdateEventRequest updateEventRequest);

    Event getFullInfoEventForUser(long userId, long eventId);

    Event cancelEvent(long userId, long eventId);

    List<ParticipationRequest> getRequestsInfoInEvents(long userId, long eventId);

    ParticipationRequest confirmRequestInEvents(long userId, long eventId, long reqId);

    ParticipationRequest rejectRequestInEvents(long userId, long eventId, long reqId);

    Event redactionEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    Event publishEvent(long eventId);

    Event rejectedEvent(long eventId);
}
