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

    private final AdminEvenService adminEvenService;
    private final EventMapper eventMapper;

    public AdminEventController(AdminEvenService adminEvenService,
                                EventMapper eventMapper) {
        this.adminEvenService = adminEvenService;
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
        List<Event> events = adminEvenService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        return events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/admin/events/{eventId}")
    public EventFullDto redactionEvent(@PathVariable @Positive Long eventId,
                                       @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = adminEvenService.redactionEvent(eventId, adminUpdateEventRequest);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping(value = "/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @Positive Long eventId) {
        Event event = adminEvenService.publishEvent(eventId);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping(value = "/admin/events/{eventId}/reject")
    public EventFullDto rejectedEvent(@PathVariable @Positive Long eventId) {
        Event event = adminEvenService.rejectedEvent(eventId);
        return eventMapper.toEventFullDto(event);
    }


}
