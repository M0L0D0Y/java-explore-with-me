package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CompilationDto {
    @NotNull
    private List<EventShortDto> events;
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank(message = "Нет названия")
    @Size(max = 100, message = "Превышен лимит символов")
    private String title;
}
