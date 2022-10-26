package ru.practicum.mainservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.common.Create;
import ru.practicum.mainservice.common.Update;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.participationRequest.ParticipationRequest;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.mainservice.participationRequest.dto.ParticipationRequestMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper requestMapper;


    @Autowired
    public EventController(EventService eventService,
                           EventMapper eventMapper,
                           ParticipationRequestMapper requestMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.requestMapper = requestMapper;
    }


    @GetMapping(value = "/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("servis: {}", "ewm-main-service");

        List<Event> eventList = eventService.getEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request);
        return eventList.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/events/{id}")
    public EventFullDto getEvent(@PathVariable @Positive Long id, HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("service: {}", "ewm-main-service");
        Event event = eventService.getEvent(id, request);
        return eventMapper.toEventFullDto(event);
    }

    @GetMapping(value = "/users/{userId}/events")
    public List<EventShortDto> getEventsForUser(@PathVariable @Positive Long userId,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        List<Event> events = eventService.getEventsForUser(userId, from, size);
        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @PatchMapping(value = "/users/{userId}/events")
    public EventFullDto updateEventForUser(@PathVariable @Positive Long userId,
                                           @RequestBody @Validated(Update.class)
                                           UpdateEventRequest updateEventRequest) {

        Event event = eventService.updateEventForUser(userId, updateEventRequest);
        return eventMapper.toEventFullDto(event);
    }

    @PostMapping(value = "/users/{userId}/events")
    private EventFullDto addEvent(@PathVariable @Positive Long userId,
                                  @RequestBody @Validated(Create.class) NewEventDto newEventDto) {
        Event event = eventMapper.toEvent(newEventDto);
        return eventMapper.toEventFullDto(eventService.addEvent(userId, event));
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto getFullInfoEventForUser(@PathVariable @Positive Long userId,
                                                @PathVariable @Positive Long eventId) {
        Event event = eventService.getFullInfoEventForUser(userId, eventId);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        Event event = eventService.cancelEvent(userId, eventId);
        return eventMapper.toEventFullDto(event);

    }

    @GetMapping(value = "/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsInfoInEvents(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long eventId) {
        List<ParticipationRequest> requests = eventService.getRequestsInfoInEvents(userId, eventId);
        return requests.stream()
                .map(requestMapper::toParRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestInEvents(@PathVariable @Positive Long userId,
                                                          @PathVariable @Positive Long eventId,
                                                          @PathVariable @Positive Long reqId) {
        ParticipationRequest request = eventService.confirmRequestInEvents(userId, eventId, reqId);
        return requestMapper.toParRequestDto(request);
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestInEvents(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId,
                                                         @PathVariable @Positive Long reqId) {
        ParticipationRequest request = eventService.rejectRequestInEvents(userId, eventId, reqId);
        return requestMapper.toParRequestDto(request);
    }

    @GetMapping(value = "/admin/events")
    public List<EventFullDto> findEvents(@RequestParam(required = false) List<Long> users,
                                         @RequestParam(required = false) List<String> states,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "0") @Positive Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<Event> events = eventService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        return events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/admin/events/{eventId}")
    public EventFullDto redactionEvent(@PathVariable @Positive Long eventId,
                                       @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventService.redactionEvent(eventId, adminUpdateEventRequest);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping(value = "/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @Positive Long eventId) {
        Event event = eventService.publishEvent(eventId);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping(value = "/admin/events/{eventId}/reject")
    public EventFullDto rejectedEvent(@PathVariable @Positive Long eventId) {
        Event event = eventService.rejectedEvent(eventId);
        return eventMapper.toEventFullDto(event);
    }
}
