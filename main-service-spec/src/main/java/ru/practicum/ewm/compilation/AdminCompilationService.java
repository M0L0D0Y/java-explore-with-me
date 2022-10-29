package ru.practicum.ewm.compilation;

public interface AdminCompilationService {
    Compilation addCompilations(Compilation compilation);

    void deleteCompilations(long compId);

    void deleteEventOfCompilation(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void unpinCompilation(long compId);

    void pinCompilation(long compId);
}
