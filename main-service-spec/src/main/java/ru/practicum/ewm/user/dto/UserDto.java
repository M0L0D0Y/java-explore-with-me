package ru.practicum.ewm.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @Email
    private String email;
    private Long id;
    @NotBlank
    private String name;
}
