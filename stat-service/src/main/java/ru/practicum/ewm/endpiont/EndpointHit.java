package ru.practicum.ewm.endpiont;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 50, message = "Превышен лимит символов")
    @Column(name = "app")
    private String app;
    @NotBlank(message = "Не указан uri обновляемого события")
    @Size(max = 100, message = "Превышен лимит символов")
    @Column(name = "uri")
    private String uri;
    @NotBlank(message = "Не указан ip обновляемого события")
    @Size(max = 50, message = "Превышен лимит символов")
    @Column(name = "ip")
    private String ip;
    @NotNull(message = "Не указан timestamp обновляемого события")
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
