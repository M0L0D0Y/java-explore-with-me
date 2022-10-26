package ru.practicum.statservice.service;

import ru.practicum.statservice.model.EndpointHit;
import ru.practicum.statservice.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    void addEndpoint(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
