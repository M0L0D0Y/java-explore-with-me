package ru.practicum.ewm.user.dto;

import lombok.*;
import ru.practicum.ewm.common.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewUserRequest {
    @Email(groups = {Create.class})
    @NotBlank(groups = {Create.class}, message = "Нет email пользователя")
    private String email;
    @NotBlank(groups = {Create.class}, message = "Нет имени пользователя")
    @Size(max = 40, message = "Превышен лимит символов")
    private String name;
}
