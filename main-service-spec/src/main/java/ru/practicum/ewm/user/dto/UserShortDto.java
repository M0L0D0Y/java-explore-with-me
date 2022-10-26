package ru.practicum.ewm.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserShortDto {
    private Long id;
    @NotBlank
    private String name;
}
