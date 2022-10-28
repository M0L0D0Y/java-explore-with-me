package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewCompilationDto {
    @NotNull
    private List<Long> events;
    @NotNull
    private Boolean pinned;
    @NotBlank(message = "Нет названия")
    @Size(max = 100, message = "Превышен лимит символов")
    private String title;
}
