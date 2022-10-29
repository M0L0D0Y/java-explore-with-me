package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;

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
    private final CommonMethods commonMethods;

    @Autowired
    public CompilationController(CompilationService compilationService,
                                 CompilationMapper mapper,
                                 CommonMethods commonMethods) {
        this.compilationService = compilationService;
        this.mapper = mapper;
        this.commonMethods = commonMethods;
    }

    @GetMapping(value = "compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "10") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = commonMethods.getPageable(from, size);
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


}
