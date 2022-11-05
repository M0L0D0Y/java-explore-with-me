package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.EndpointHitMapper;
import ru.practicum.ewm.model.NewEndpointDto;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Validated
public class EndpointController {
    private final EndpointHitService endpointHitService;
    private final EndpointHitMapper mapper;

    @Autowired
    public EndpointController(EndpointHitService endpointHitService,
                              EndpointHitMapper mapper) {
        this.endpointHitService = endpointHitService;
        this.mapper = mapper;
    }

    @PostMapping(value = "/hit")
    public void addEndpoint(@RequestBody NewEndpointDto newEndpointDto) {
        EndpointHit endpointHit = mapper.toEndpointHit(newEndpointDto);
        endpointHitService.addEndpoint(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        return endpointHitService.getStats(start, end, uris, unique);
    }
}
