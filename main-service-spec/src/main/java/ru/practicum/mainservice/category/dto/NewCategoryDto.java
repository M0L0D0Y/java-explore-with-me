package ru.practicum.mainservice.category.dto;

import lombok.Data;
import ru.practicum.mainservice.common.Create;

import javax.validation.constraints.NotBlank;

@Data
public class NewCategoryDto {
    @NotBlank(groups = Create.class, message = "Нет названия категории")
    private String name;
}
