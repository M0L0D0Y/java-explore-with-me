package ru.practicum.ewm.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryStorage;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.client.ViewStats;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationStorage;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventStorage;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.exception.ConflictDataTimeException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.UnavailableException;
import ru.practicum.ewm.participationRequest.ParticipationRequest;
import ru.practicum.ewm.participationRequest.ParticipationRequestStorage;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserStorage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class CommonMethods {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat(PATTERN);
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final ParticipationRequestStorage requestStorage;
    private final CategoryStorage categoryStorage;
    private final CompilationStorage compilationStorage;

    private final StatClient client;

    @Autowired
    public CommonMethods(UserStorage userStorage,
                         EventStorage eventStorage,
                         ParticipationRequestStorage requestStorage,
                         CategoryStorage categoryStorage,
                         CompilationStorage compilationStorage,
                         StatClient client) {
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
        this.requestStorage = requestStorage;
        this.categoryStorage = categoryStorage;
        this.compilationStorage = compilationStorage;
        this.client = client;
    }

    public LocalDateTime toLocalDataTime(String dateTime) {
        try {
            Date date = sdf.parse(dateTime);
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(LocalDateTime localDateTime) {
        Date dateToConvert = java.sql.Timestamp.valueOf(localDateTime);
        try {
            Date date = sdf.parse(sdf.format(dateToConvert));
            return sdf.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public User checkExistUser(long id) {
        return userStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id {}" + id));
    }

    public Event checkExistEvent(long id) {
        return eventStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет события с таким id " + id));
    }

    public ParticipationRequest checkExistRequest(long id) {
        return requestStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет заявки с таким id " + id));
    }

    public Category checkExistCategory(long id) {
        return categoryStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Категории с таким id нет " + id));
    }

    public Compilation checkExistCompilation(long id) {
        return compilationStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет подборки с id = " + id));
    }

    public Predicate buildingPredicateForSearchByStart(String rangeStart,
                                                       CriteriaBuilder cb,
                                                       Root<Event> eventRoot) {
        Predicate predicate;
        if (!(rangeStart.isEmpty())) {
            predicate = cb.greaterThan(eventRoot.get("eventDate"),
                    cb.literal(toLocalDataTime(rangeStart)));
        } else {
            predicate = cb.greaterThan(eventRoot.get("eventDate"),
                    cb.literal(LocalDateTime.now()));
        }
        return predicate;
    }

    public Predicate buildingPredicateForSearchByEnd(String rangeEnd,
                                                     CriteriaBuilder cb,
                                                     Root<Event> eventRoot) {
        Predicate predicate;
        if (!(rangeEnd.isEmpty())) {
            predicate = cb.lessThan(eventRoot.get("eventDate"),
                    cb.literal(toLocalDataTime(rangeEnd)));
        } else {
            predicate = cb.lessThan(eventRoot.get("eventDate"),
                    cb.literal(LocalDateTime.now()));
        }
        return predicate;
    }

    public Event updateEvent(Event oldEvent, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime eventDate = toLocalDataTime(updateEventRequest.getEventDate());
            oldEvent.setEventDate(eventDate);
            log.info("Обновили дату события на " + eventDate);
        }
        if (updateEventRequest.getPaid() != null) {
            oldEvent.setPaid(updateEventRequest.getPaid());
            log.info("Обновили флаг платности события на " + updateEventRequest.getPaid());
        }
        if (updateEventRequest.getAnnotation() != null) {
            oldEvent.setAnnotation(updateEventRequest.getAnnotation());
            log.info("Обновили краткое описание события на " + updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryStorage.findById(updateEventRequest.getCategory())
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new NotFoundException("Категории с таким id нет " +
                            updateEventRequest.getCategory()));
            oldEvent.setCategory(category);
            log.info("Обновили категорию события на " + category);
        }
        if (updateEventRequest.getDescription() != null) {
            oldEvent.setDescription(updateEventRequest.getDescription());
            log.info("Обновили полное описание события");
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEventRequest.getParticipantLimit());
            log.info("Обновили лимит пользователей на " + updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            oldEvent.setTitle(updateEventRequest.getTitle());
            log.info("Обновили заголовок события на " + updateEventRequest.getTitle());
        }
        return oldEvent;
    }

    public void checkDateTime(LocalDateTime start, Event event) {
        if (start.isAfter(event.getEventDate())) {
            throw new ConflictDataTimeException("Время старта события в прошлом");
        }
        Duration duration = Duration.between(start, event.getEventDate());
        if (duration.toHours() < 2) {
            throw new ConflictDataTimeException("Время старта события раньше, чем 2 часа от текущего времени");
        }
    }

    public Pageable getPageable(int from, int size) {
        if ((from < 0) || (size < 1)) {
            throw new UnavailableException("неправильно заданы параметры запроса " +
                    "индекс " + from + " количество элементов " + size);
        }
        int page;
        if (from == 0) {
            page = 0;
        } else {
            page = size / from;
        }
        return PageRequest.of(page, size);
    }

    public void sendToStatServer(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String service = "ewm-main-service";
        String time = toString(LocalDateTime.now());
        log.info("ip = {}, uri = {}, service = {}, time = {}", ip, uri, service, time);
        client.addEndpoint(ip, uri, service, time);
    }

    public Long getViews(Event event) {
        Boolean unique = false;
        ViewStats views = client.getEndpoint(event.getPublishedOn(), LocalDateTime.now(), event.getId(), unique);
        log.info("Данные из статистики {}", views);
        return views != null ? views.getHits() : 0L;
    }
}
