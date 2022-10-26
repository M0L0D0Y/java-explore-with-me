package ru.practicum.ewm.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    @NotNull(message = "Отсутствует идентификатор")
    private Long id;
    @NotBlank(message = "Не указано название категории")
    private String name;

}
