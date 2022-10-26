package ru.practicum.statservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.transfer.client.NewEndpointHitDto;
import ru.practicum.statservice.model.EndpointHit;

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
