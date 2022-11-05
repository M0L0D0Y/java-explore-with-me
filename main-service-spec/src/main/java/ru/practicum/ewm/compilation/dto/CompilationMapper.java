package ru.practicum.ewm.compilation.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventStorage;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CompilationMapper {
    private final EventStorage eventStorage;
    private final EventMapper eventMapper;

    @Autowired
    public CompilationMapper(EventStorage eventStorage, EventMapper eventMapper) {
        this.eventStorage = eventStorage;
        this.eventMapper = eventMapper;
    }

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        List<Long> eventsId = newCompilationDto.getEvents();
        Set<Event> events = eventStorage.findEventsForCompilation(eventsId);
        compilation.setEvents(events);
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        Set<Event> setEvents = compilation.getEvents();
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        if (!setEvents.isEmpty()) {
            List<Event> events = new ArrayList<>(setEvents);
            eventShortDtos = eventMapper.toListEventShortDto(events);
        }
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }
}
