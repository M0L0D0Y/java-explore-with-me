package ru.practicum.ewm.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CategoryDto {
    @NotNull(message = "Отсутствует идентификатор")
    private Long id;
    @NotBlank(message = "Не указано название категории")
    @Size(max = 50, message = "Превышен лимит символов")
    private String name;

}
