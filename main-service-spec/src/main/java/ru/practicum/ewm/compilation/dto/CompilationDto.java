package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompilationDto {
    @NotNull
    private List<EventShortDto> events;
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}
