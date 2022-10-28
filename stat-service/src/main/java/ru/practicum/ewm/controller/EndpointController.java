package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.EndpointHitMapper;
import ru.practicum.ewm.model.NewEndpointDto;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.service.ConverterDataTime;
import ru.practicum.ewm.service.EndpointHitService;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Validated
public class EndpointController {
    private final EndpointHitService endpointHitService;
    private final EndpointHitMapper mapper;
    private final ConverterDataTime converterDataTime;

    @Autowired
    public EndpointController(EndpointHitService endpointHitService,
                              EndpointHitMapper mapper,
                              ConverterDataTime converterDataTime) {
        this.endpointHitService = endpointHitService;
        this.mapper = mapper;
        this.converterDataTime = converterDataTime;
    }

    @PostMapping(value = "/hit")
    public void addEndpoint(@RequestBody NewEndpointDto newEndpointDto) {
        EndpointHit endpointHit = mapper.toEndpointHit(newEndpointDto);
        endpointHitService.addEndpoint(endpointHit);
    }

    @GetMapping(value = "/stats")
    public List<ViewStats> getStats(@RequestParam @NotBlank String start,
                                    @RequestParam @NotBlank String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startDateTime = converterDataTime.toLocalDataTime(start);
        LocalDateTime endDateTime = converterDataTime.toLocalDataTime(end);
        return endpointHitService.getStats(startDateTime, endDateTime, uris, unique);
    }
}
