package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.Create;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping
@Slf4j
@Validated
public class AdminCompilationController {
    private final CompilationMapper mapper;
    private final AdminCompilationService adminService;

    @Autowired
    public AdminCompilationController(CompilationMapper mapper, AdminCompilationService adminService) {
        this.mapper = mapper;
        this.adminService = adminService;
    }

    @PostMapping(value = "/admin/compilations")
    public CompilationDto addCompilations(@RequestBody @Validated(Create.class) NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.toCompilation(newCompilationDto);
        CompilationDto compilationDto = mapper.toCompilationDto(adminService.addCompilations(compilation));
        log.info("{}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping(value = "/admin/compilations/{compId}")
    public String deleteCompilations(@PathVariable @Positive Long compId) {
        adminService.deleteCompilations(compId);
        return "Подборка удалена";
    }

    @DeleteMapping(value = "/admin/compilations/{compId}/events/{eventId}")
    public String deleteEventOfCompilation(@PathVariable @Positive Long compId,
                                           @PathVariable @Positive Long eventId) {
        adminService.deleteEventOfCompilation(compId, eventId);
        return "Событие удалено из подборки";
    }

    @PatchMapping(value = "/admin/compilations/{compId}/events/{eventId}")
    public String addEventInCompilation(@PathVariable @Positive Long compId,
                                        @PathVariable @Positive Long eventId) {
        adminService.addEventInCompilation(compId, eventId);
        return "Событие добавлено";
    }

    @DeleteMapping(value = "/admin/compilations/{compId}/pin")
    public String unpinCompilation(@PathVariable @Positive Long compId) {
        adminService.unpinCompilation(compId);
        return "Подборка откреплена";
    }

    @PatchMapping(value = "/admin/compilations/{compId}/pin")
    public String pinCompilation(@PathVariable @Positive Long compId) {
        adminService.pinCompilation(compId);
        return "Подборка закреплена";
    }
}
