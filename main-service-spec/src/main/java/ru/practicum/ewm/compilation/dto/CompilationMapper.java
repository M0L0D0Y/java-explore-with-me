package ru.practicum.ewm.compilation.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Event> events = compilation.getEvents();
        List<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::toEventShortDto).collect(Collectors.toList());
        compilationDto.setEvents(eventShortDtoList);
        return compilationDto;
    }
}
