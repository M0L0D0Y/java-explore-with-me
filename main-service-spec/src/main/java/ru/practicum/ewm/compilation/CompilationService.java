package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompilationService {
    List<Compilation> getCompilations(Boolean pinned, Pageable pageable);

    Compilation getCompilation(long compId);


}
