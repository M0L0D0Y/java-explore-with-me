package ru.practicum.ewm.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class AdminEventController {

    private final AdminEventService adminEventService;
    private final EventMapper eventMapper;

    public AdminEventController(AdminEventService adminEventService,
                                EventMapper eventMapper) {
        this.adminEventService = adminEventService;
        this.eventMapper = eventMapper;
    }

    @GetMapping(value = "/admin/events")
    public List<EventFullDto> findEvents(@RequestParam(required = false) List<Long> users,
                                         @RequestParam(required = false) List<String> states,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "0") @Positive Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<Event> events = adminEventService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtos = events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
        log.info("{}", eventFullDtos);
        return eventFullDtos;
    }

    @PutMapping(value = "/admin/events/{eventId}")
    public EventFullDto redactionEvent(@PathVariable @Positive Long eventId,
                                       @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = adminEventService.redactionEvent(eventId, adminUpdateEventRequest);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping(value = "/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @Positive Long eventId) {
        Event event = adminEventService.publishEvent(eventId);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping(value = "/admin/events/{eventId}/reject")
    public EventFullDto rejectedEvent(@PathVariable @Positive Long eventId) {
        Event event = adminEventService.rejectedEvent(eventId);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("{}", eventFullDto);
        return eventFullDto;
    }
}
