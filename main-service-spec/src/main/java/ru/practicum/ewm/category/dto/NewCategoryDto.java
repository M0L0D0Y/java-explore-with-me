package ru.practicum.ewm.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.common.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewCategoryDto {
    @NotBlank(groups = Create.class, message = "Нет названия категории")
    @Size(max = 50, message = "Превышено допустимое количество символов в имени")
    private String name;
}
