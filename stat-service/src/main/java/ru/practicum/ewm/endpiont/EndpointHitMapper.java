package ru.practicum.ewm.endpiont;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EndpointHitMapper {
    private final ConverterDataTime converterDataTime;

    @Autowired
    public EndpointHitMapper(ConverterDataTime converterDataTime) {
        this.converterDataTime = converterDataTime;
    }

    public EndpointHit toEndpointHit(NewEndpointDto newEndpointDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(newEndpointDto.getApp());
        endpointHit.setIp(newEndpointDto.getIp());
        endpointHit.setUri(newEndpointDto.getUri());
        LocalDateTime timestamp = converterDataTime.toLocalDataTime(newEndpointDto.getTimestamp());
        endpointHit.setTimestamp(timestamp);
        return endpointHit;
    }
}
