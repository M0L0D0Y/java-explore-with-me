package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;

@Service
public class StatClient extends BaseClient {
    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public void addEndpoint(String ip, String uri, String service, String time) {
        String path = "/hit";
        EndpointDto endpointDto = new EndpointDto();
        endpointDto.setApp(service);
        endpointDto.setUri(uri);
        endpointDto.setIp(ip);
        endpointDto.setTimestamp(time);
        postHit(path, endpointDto);
    }

    public ViewStats getEndpoint(LocalDateTime start, LocalDateTime end, Long eventId, Boolean unique) {
        String path = "/stats?start=%s&end=%s&uris=%s&unique=%s";
        return getStats(path, start, end, eventId, unique);
    }
}
