package ru.practicum.ewm.transfer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.Create;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.NewEndpointHitDto;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.service.ConverterDataTime;
import ru.practicum.ewm.service.EndpointHitService;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class EndpointController {
    private final EndpointHitService service;
    private final EndpointHitMapper mapper;
    private final ConverterDataTime converterDataTime;

    @Autowired
    public EndpointController(EndpointHitService service,
                              EndpointHitMapper mapper,
                              ConverterDataTime converterDataTime) {
        this.service = service;
        this.mapper = mapper;
        this.converterDataTime = converterDataTime;
    }

    @PostMapping(value = "/hit")
    public void addEndpoint(@RequestBody @Validated(Create.class) NewEndpointHitDto newEndpointHitDto) {
        EndpointHit endpointHit = mapper.toEndpointHit(newEndpointHitDto);
        service.addEndpoint(endpointHit);
        //return "Информация сохранена";
    }

    @GetMapping(value = "/stats")
    public List<ViewStats> getStats(@RequestParam @NotBlank String start,
                                    @RequestParam @NotBlank String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startDateTime = converterDataTime.toLocalDataTime(start);
        LocalDateTime endDateTime = converterDataTime.toLocalDataTime(end);
        return service.getStats(startDateTime, endDateTime, uris, unique);

    }
}
