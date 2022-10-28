package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

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
        post(path, endpointDto);
    }

    public ResponseEntity<Object> getEndpoint(String start, String end, List<String> uris, Boolean unique) {
        return get(start, end, uris, unique);
    }


}
