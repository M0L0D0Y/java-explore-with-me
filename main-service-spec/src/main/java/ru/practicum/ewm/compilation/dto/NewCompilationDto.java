package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
    @NotNull
    private List<Long> events;
    @NotNull
    private Boolean pinned;
    @NotBlank
    @NotNull
    @Size(max = 100, message = "Превышено допустимое количество символов в названии")
    private String title;
}
