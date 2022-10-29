package ru.practicum.ewm.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;
    @NotBlank(message = "Нет имени пользователя")
    @Size(max = 40, message = "Превышен лимит символов")
    private String name;
}
