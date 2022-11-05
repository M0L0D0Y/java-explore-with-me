package ru.practicum.ewm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class Client {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected final RestTemplate rest;

    public Client(RestTemplate rest) {
        this.rest = rest;
    }

    public void postHit(String path, EndpointDto hit) {
        rest.postForEntity(path, hit, EndpointDto.class);
    }

    public List<ViewStats> getStats(String path,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    List<Long> eventIds,
                                    Boolean unique) {
        List<String> uris = new ArrayList<>();
        for (Long eventId : eventIds) {
            uris.add("/events/" + eventId);
        }
        ViewStats[] stats = rest.getForObject(
                String.format(
                        path,
                        start.format(formatter),
                        end.format(formatter),
                        uris,
                        unique),
                ViewStats[].class
        );
        if (stats != null) {
            return stats.length > 0 ? List.of(stats) : null;
        } else {
            return null;
        }
    }
}
