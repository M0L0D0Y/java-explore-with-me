package ru.practicum.mainservice.user.dto;

import lombok.Data;
import ru.practicum.mainservice.common.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewUserRequest {
    @Email(groups = {Create.class})
    @NotBlank(groups = {Create.class}, message = "Нет email пользователя")
    private String email;
    @NotBlank(groups = {Create.class}, message = "Нет имени пользователя")
    private String name;
}
