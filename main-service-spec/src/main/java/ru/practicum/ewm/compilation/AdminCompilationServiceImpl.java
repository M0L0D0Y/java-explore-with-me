package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.exception.UnavailableException;

import java.util.Set;

@Slf4j
@Service
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationStorage storage;
    private final CommonMethods commonMethods;

    @Autowired
    public AdminCompilationServiceImpl(CompilationStorage storage, CommonMethods commonMethods) {
        this.storage = storage;
        this.commonMethods = commonMethods;
    }

    @Override
    @Transactional
    public Compilation addCompilations(Compilation compilation) {
        Compilation savedCompilation = storage.save(compilation);
        log.info("Подборка сохранена");
        return savedCompilation;
    }

    @Override
    @Transactional
    public void deleteCompilations(long compId) {
        commonMethods.checkExistCompilation(compId);
        log.info("Подборка удалена");
        storage.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventOfCompilation(long compId, long eventId) {
        Compilation compilation = commonMethods.checkExistCompilation(compId);
        Event event = commonMethods.checkExistEvent(eventId);
        if (compilation.getEvents() != null) {
            if (!(compilation.getEvents().contains(event))) {
                throw new UnavailableException("Событие с id = " + eventId +
                        " не относится к подборке с id = " + compId);
            }
            Set<Event> events = compilation.getEvents();
            events.remove(event);
            compilation.setEvents(events);
            storage.save(compilation);
            log.info("Событие с id {} удалено из подборки с id {}", eventId, compId);
        } else {
            throw new UnavailableException("В подборке с id = " + compId + " нет событий");
        }
    }


    @Override
    @Transactional
    public void addEventInCompilation(long compId, long eventId) {
        Compilation compilation = commonMethods.checkExistCompilation(compId);
        Event event = commonMethods.checkExistEvent(eventId);
        if (!(event.getState().equals(EventState.PUBLISHED.toString()))) {
            throw new UnavailableException("Нельзя добавить в подборку неопубликованное событие");
        }
        Set<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        storage.save(compilation);
        log.info("Событие с id {} добавлено в подборку с id {}", eventId, compId);
    }


    @Override
    @Transactional
    public void unpinCompilation(long compId) {
        Compilation compilation = commonMethods.checkExistCompilation(compId);
        if (!compilation.getPinned()) {
            throw new UnavailableException("Подборка не закреплена на главной странице");
        }
        compilation.setPinned(false);
        storage.save(compilation);
        log.info("Подборка с id {} откреплена от главной страницы", compId);
    }

    @Override
    @Transactional
    public void pinCompilation(long compId) {
        Compilation compilation = commonMethods.checkExistCompilation(compId);
        if (compilation.getPinned()) {
            throw new UnavailableException("Подборка закреплена на главной странице");
        }
        compilation.setPinned(true);
        storage.save(compilation);
        log.info("Подборка с id {} закреплена на главной страницы", compId);
    }
}
