package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompilationService {
    List<Compilation> getCompilations(Boolean pinned, Pageable pageable);

    Compilation getCompilation(long compId);

    Compilation addCompilations(Compilation compilation);

    void deleteCompilations(long compId);

    void deleteEventOfCompilation(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void unpinCompilation(long compId);

    void pinCompilation(long compId);
}
