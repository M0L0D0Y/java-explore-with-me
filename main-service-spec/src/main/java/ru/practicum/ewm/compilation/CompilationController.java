package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.Create;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.user.Page;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class CompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper mapper;

    @Autowired
    public CompilationController(CompilationService compilationService, CompilationMapper mapper) {
        this.compilationService = compilationService;
        this.mapper = mapper;
    }

    @GetMapping(value = "compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "10") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = Page.getPageable(from, size);
        List<Compilation> compilations = compilationService.getCompilations(pinned, pageable);
        return compilations.stream()
                .map(mapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable @Positive Long compId) {
        Compilation compilation = compilationService.getCompilation(compId);
        return mapper.toCompilationDto(compilation);
    }

    @PostMapping(value = "/admin/compilations")
    public CompilationDto addCompilations(@RequestBody @Validated(Create.class) NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.toCompilation(newCompilationDto);
        return mapper.toCompilationDto(compilationService.addCompilations(compilation));
    }

    @DeleteMapping(value = "/admin/compilations/{compId}")
    public String deleteCompilations(@PathVariable @Positive Long compId) {
        compilationService.deleteCompilations(compId);
        return "Подборка удалена";
    }

    @DeleteMapping(value = "/admin/compilations/{compId}/events/{eventId}")
    public String deleteEventOfCompilation(@PathVariable @Positive Long compId,
                                           @PathVariable @Positive Long eventId) {
        compilationService.deleteEventOfCompilation(compId, eventId);
        return "Событие удалено из подборки";
    }

    @PatchMapping(value = "/admin/compilations/{compId}/events/{eventId}")
    public String addEventInCompilation(@PathVariable @Positive Long compId,
                                        @PathVariable @Positive Long eventId) {
        compilationService.addEventInCompilation(compId, eventId);
        return "Событие добавлено";
    }

    @DeleteMapping(value = "/admin/compilations/{compId}/pin")
    public String unpinCompilation(@PathVariable @Positive Long compId) {
        compilationService.unpinCompilation(compId);
        return "Подборка откреплена";
    }

    @PatchMapping(value = "/admin/compilations/{compId}/pin")
    public String pinCompilation(@PathVariable @Positive Long compId) {
        compilationService.pinCompilation(compId);
        return "Подборка закреплена";
    }
}
