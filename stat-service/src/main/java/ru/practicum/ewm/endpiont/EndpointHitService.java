package ru.practicum.ewm.endpiont;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    void addEndpoint(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
