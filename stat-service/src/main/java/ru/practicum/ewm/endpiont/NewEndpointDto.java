package ru.practicum.ewm.endpiont;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewEndpointDto {
    @NotBlank(message = "Нет названия сервиса")
    @Size(max = 50, message = "Превышен лимит символов")
    private String app;
    @NotBlank(message = "Нет пути запроса")
    @Size(max = 100, message = "Превышен лимит символов")
    private String uri;
    @NotBlank(message = "Нет ip пользователя")
    @Size(max = 50, message = "Превышен лимит символов")
    private String ip;
    @NotBlank(message = "Нет даты запроса")
    private String timestamp;
}
