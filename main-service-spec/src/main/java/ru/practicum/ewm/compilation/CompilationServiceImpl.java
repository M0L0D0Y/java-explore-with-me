package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.ecxeption.NotFoundException;
import ru.practicum.ewm.ecxeption.UnavailableException;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventState;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationStorage storage;
    private final CommonMethods commonMethods;

    @Autowired
    public CompilationServiceImpl(CompilationStorage storage, CommonMethods commonMethods) {
        this.storage = storage;
        this.commonMethods = commonMethods;
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = storage.findAllCompilations(pinned, pageable);
        } else {
            compilations = storage.findAllCompilations(pageable);
        }
        log.info("Найдены все подборки");
        return compilations;
    }

    @Override
    public Compilation getCompilation(long compId) {
        Compilation compilation = checkExist(compId);
        log.info("Подборка событий с id = " + compId + " найдена");
        return compilation;
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
        checkExist(compId);
        log.info("Подборка удалена");
        storage.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventOfCompilation(long compId, long eventId) {
        Compilation compilation = checkExist(compId);
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
        Compilation compilation = checkExist(compId);
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
        Compilation compilation = checkExist(compId);
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
        Compilation compilation = checkExist(compId);
        if (compilation.getPinned()) {
            throw new UnavailableException("Подборка закреплена на главной странице");
        }
        compilation.setPinned(true);
        storage.save(compilation);
        log.info("Подборка с id {} закреплена на главной страницы", compId);
    }

    private Compilation checkExist(long id) {
        Compilation compilation = storage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет подборки с id = " + id));
        log.info("Найдена подборка с id {}", id);
        return compilation;
    }
}
