package ru.practicum.ewm.participationRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm.participationRequest.dto.ParticipationRequestMapper;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class ParticipationRequestController {
    private final ParticipationRequestService service;
    private final ParticipationRequestMapper mapper;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService service,
                                          ParticipationRequestMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(value = "/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequestInfoForUser(@PathVariable @Positive Long userId) {
        List<ParticipationRequest> requestList = service.getRequestInfoForUser(userId);
        List<ParticipationRequestDto> requestDtoList = requestList.stream()
                .map(mapper::toParRequestDto)
                .collect(Collectors.toList());
        log.info("{}", requestDtoList);
        return requestDtoList;
    }

    @PostMapping(value = "/users/{userId}/requests")
    public ParticipationRequestDto addRequestForEvent(@PathVariable @Positive Long userId,
                                                      @RequestParam @Positive Long eventId) {
        ParticipationRequest request = service.addRequestForEvent(userId, eventId);
        ParticipationRequestDto requestDto = mapper.toParRequestDto(request);
        log.info("{}", requestDto);
        return requestDto;
    }

    @PatchMapping(value = "/users/{userId}/requests/{requestsId}/cancel")
    public ParticipationRequestDto cancelRequestForEvent(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long requestsId) {
        ParticipationRequest request = service.cancelRequestForEvent(userId, requestsId);
        ParticipationRequestDto requestDto = mapper.toParRequestDto(request);
        log.info("{}", requestDto);
        return requestDto;
    }
}
