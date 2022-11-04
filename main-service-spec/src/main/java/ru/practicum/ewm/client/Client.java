package ru.practicum.ewm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ViewStats getStats(String path, LocalDateTime start, LocalDateTime end, Long eventId, Boolean unique) {
        ViewStats[] stats = rest.getForObject(
                String.format(
                        path,
                        start.format(formatter),
                        end.format(formatter),
                        List.of(URLEncoder.encode("/events/" + eventId, StandardCharsets.UTF_8)),
                        unique),
                ViewStats[].class
        );
        if (stats != null) {
            return stats.length > 0 ? stats[0] : null;
        } else {
            return null;
        }
    }
}
