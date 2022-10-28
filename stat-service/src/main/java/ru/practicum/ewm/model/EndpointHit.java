package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "endpoints")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Не указан app обновляемого события")
    @Column(name = "app")
    private String app;
    @NotBlank(message = "Не указан uri обновляемого события")
    @Column(name = "uri")
    private String uri;
    @NotBlank(message = "Не указан ip обновляемого события")
    @Column(name = "ip")
    private String ip;
    @NotNull(message = "Не указан timestamp обновляемого события")
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
