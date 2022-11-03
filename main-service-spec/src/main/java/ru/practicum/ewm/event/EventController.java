package ru.practicum.ewm.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.common.Create;
import ru.practicum.ewm.common.Update;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.participationRequest.ParticipationRequest;
import ru.practicum.ewm.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm.participationRequest.dto.ParticipationRequestMapper;

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

    private final CommonMethods commonMethods;


    @Autowired
    public EventController(EventService eventService,
                           EventMapper eventMapper,
                           ParticipationRequestMapper requestMapper,
                           CommonMethods commonMethods) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.requestMapper = requestMapper;
        this.commonMethods = commonMethods;
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
        commonMethods.sendToStatServer(request);
        log.info("Отправили пакет данных в статистику");
        List<Event> eventList = eventService.getEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        List<EventShortDto> eventShortDtos = eventList.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        log.info("{}", eventShortDtos);
        return eventShortDtos;
    }

    @GetMapping(value = "/events/{id}")
    public EventFullDto getEvent(@PathVariable @Positive Long id, HttpServletRequest request) {
        commonMethods.sendToStatServer(request);
        log.info("Отправили пакет данных в статистику");
        Event event = eventService.getEvent(id);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping(value = "/users/{userId}/events")
    public List<EventShortDto> getEventsForUser(@PathVariable @Positive Long userId,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        List<Event> events = eventService.getEventsForUser(userId, from, size);
        List<EventShortDto> eventShortDtos = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        log.info("{}", eventShortDtos);
        return eventShortDtos;
    }

    @PatchMapping(value = "/users/{userId}/events")
    public EventFullDto updateEventForUser(@PathVariable @Positive Long userId,
                                           @RequestBody @Validated(Update.class)
                                           UpdateEventRequest updateEventRequest) {

        Event event = eventService.updateEventForUser(userId, updateEventRequest);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;
    }

    @PostMapping(value = "/users/{userId}/events")
    private EventFullDto addEvent(@PathVariable @Positive Long userId,
                                  @RequestBody @Validated(Create.class) NewEventDto newEventDto) {
        Event event = eventMapper.toEvent(newEventDto);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventService.addEvent(userId, event));
        log.info("{}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto getFullInfoEventForUser(@PathVariable @Positive Long userId,
                                                @PathVariable @Positive Long eventId) {
        Event event = eventService.getFullInfoEventForUser(userId, eventId);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        Event event = eventService.cancelEvent(userId, eventId);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;

    }

    @GetMapping(value = "/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsInfoInEvents(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long eventId) {
        List<ParticipationRequest> requests = eventService.getRequestsInfoInEvents(userId, eventId);
        List<ParticipationRequestDto> requestDtoList = requests.stream()
                .map(requestMapper::toParRequestDto)
                .collect(Collectors.toList());
        log.info("{}", requestDtoList);
        return requestDtoList;
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestInEvents(@PathVariable @Positive Long userId,
                                                          @PathVariable @Positive Long eventId,
                                                          @PathVariable @Positive Long reqId) {
        ParticipationRequest request = eventService.confirmRequestInEvents(userId, eventId, reqId);
        ParticipationRequestDto requestDto = requestMapper.toParRequestDto(request);
        log.info("{}", requestDto);
        return requestDto;
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestInEvents(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId,
                                                         @PathVariable @Positive Long reqId) {
        ParticipationRequest request = eventService.rejectRequestInEvents(userId, eventId, reqId);
        ParticipationRequestDto requestDto = requestMapper.toParRequestDto(request);
        log.info("{}", requestDto);
        return requestDto;
    }
}
