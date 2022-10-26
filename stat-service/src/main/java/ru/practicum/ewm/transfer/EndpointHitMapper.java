package ru.practicum.ewm.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.NewEndpointHitDto;
import ru.practicum.ewm.service.ConverterDataTime;

import java.time.LocalDateTime;

@Service
public class EndpointHitMapper {
    private final ConverterDataTime converterDataTime;

    @Autowired
    public EndpointHitMapper(ConverterDataTime converterDataTime) {
        this.converterDataTime = converterDataTime;
    }

    public EndpointHit toEndpointHit(NewEndpointHitDto newEndpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(newEndpointHitDto.getApp());
        endpointHit.setIp(newEndpointHitDto.getIp());
        endpointHit.setUri(newEndpointHitDto.getUri());
        LocalDateTime timestamp = converterDataTime.toLocalDataTime(newEndpointHitDto.getTimestamp());
        endpointHit.setTimestamp(timestamp);
        return endpointHit;
    }
}
