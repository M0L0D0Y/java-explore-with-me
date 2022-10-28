package ru.practicum.ewm.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserDto {
    @Email
    @NotBlank(message = "Нет email пользователя")
    private String email;
    private Long id;
    @NotBlank(message = "Нет имени пользователя")
    @Size(max = 40, message = "Превышен лимит символов")
    private String name;
}
