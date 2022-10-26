package ru.practicum.mainservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.category.Category;
import ru.practicum.mainservice.category.CategoryStorage;
import ru.practicum.mainservice.common.CommonMethods;
import ru.practicum.mainservice.ecxeption.*;
import ru.practicum.mainservice.event.dto.AdminUpdateEventRequest;
import ru.practicum.mainservice.event.dto.UpdateEventRequest;
import ru.practicum.mainservice.participationRequest.ParticipationRequest;
import ru.practicum.mainservice.participationRequest.ParticipationRequestStorage;
import ru.practicum.mainservice.participationRequest.RequestStatus;
import ru.practicum.mainservice.user.Page;
import ru.practicum.mainservice.user.User;
import ru.practicum.transfer.client.NewEndpointHitDto;
import ru.practicum.transfer.client.StatClient;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EntityManager entityManager;
    private final EventStorage eventStorage;

    private final CategoryStorage categoryStorage;
    private final ParticipationRequestStorage requestStorage;
    private final CommonMethods commonMethods;
    private final StatClient client;

    @Autowired
    public EventServiceImpl(EntityManager entityManager, EventStorage eventStorage,
                            CategoryStorage categoryStorage,
                            ParticipationRequestStorage requestStorage,
                            CommonMethods commonMethods, StatClient client) {
        this.entityManager = entityManager;
        this.eventStorage = eventStorage;
        this.categoryStorage = categoryStorage;
        this.requestStorage = requestStorage;
        this.commonMethods = commonMethods;
        this.client = client;
    }

    @Override
    @Transactional
    public Event addEvent(long userId, Event event) {
        commonMethods.checkExistUser(userId);
        addField(userId, event);
        Event newEvent = eventStorage.save(event);
        log.info("Событие сохранено");
        return newEvent;
    }

    @Override
    public Event getEvent(long id, HttpServletRequest request) {
        Event event = commonMethods.checkExistEvent(id);
        if (!(event.getState().equals(EventState.PUBLISHED.toString()))) {
            throw new UnavailableException("Событие не опубликовано");
        }
        event.setViews(event.getViews() + 1);
        //передача инфы в статистику
        sendToStatServer(request);
        return event;
    }

    @Override
    public List<Event> getEvents(String text, List<Integer> categories, Boolean paid,
                                 String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                 String sort, Integer from, Integer size,
                                 HttpServletRequest request) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> eventRoot = cq.from(Event.class);
        List<Predicate> predicates = new LinkedList<>();
        Predicate available;
        if (!onlyAvailable) {
            available = cb.le(eventRoot.get("confirmedRequests"), eventRoot.get("participantLimit"));
            predicates.add(available);
        }
        if (onlyAvailable) {
            available = cb.lt(eventRoot.get("confirmedRequests"), eventRoot.get("participantLimit"));
            predicates.add(available);
        }
        if (!(text.isEmpty())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(eventRoot.get("annotation")), "%" + text.toLowerCase() + "%"),
                    cb.like(cb.lower(eventRoot.get("description")), "%" + text.toLowerCase() + "%")));
        }
        if (!(categories.isEmpty())) {
            predicates.add(eventRoot.get("category").get("id").in(categories));
        }
        if (paid != null) {
            if (paid) {
                Predicate searchPaid = cb.equal(eventRoot.get("paid"), true);
                predicates.add(searchPaid);
            }
            if (!(paid)) {
                Predicate searchPaid = cb.equal(eventRoot.get("paid"), false);
                predicates.add(searchPaid);
            }
        }
        predicates.add(buildingPredicateForSearchByStart(rangeStart, cb, eventRoot));
        predicates.add(buildingPredicateForSearchByEnd(rangeEnd, cb, eventRoot));
        Predicate[] param = new Predicate[predicates.size()];
        for (int i = 0; i < predicates.size(); i++) {
            param[i] = predicates.get(i);
        }
        if (!(sort.isEmpty())) {
            if (sort.equals(EventSort.EVENT_DATE.toString())) {
                cq.select(eventRoot).where(param).orderBy(cb.asc(eventRoot.get("eventDate")));
            }
            if (sort.equals(EventSort.VIEWS.toString())) {
                cq.select(eventRoot).where(param).orderBy(cb.asc(eventRoot.get("views")));
            }
        }
        TypedQuery<Event> query = entityManager.createQuery(cq);
        List<Event> results = query.setMaxResults(size).setFirstResult(from).getResultList();
        log.info("Найдены все события соответствующие заданным фильтрам");
        for (Event event : results) {
            event.setViews(event.getViews() + 1);
        }
        sendToStatServer(request);
        return results;
    }

    @Override
    public List<Event> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                  String rangeStart, String rangeEnd, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> eventRoot = cq.from(Event.class);
        List<Predicate> predicates = new LinkedList<>();

        if (!(users.isEmpty())) {
            predicates.add(eventRoot.get("initiator").get("id").in(users));
        }
        if (!(states.isEmpty())) {
            predicates.add(eventRoot.get("state").in(states));
        }
        if (!(categories.isEmpty())) {
            predicates.add(eventRoot.get("category").in(categories));
        }
        predicates.add(buildingPredicateForSearchByStart(rangeStart, cb, eventRoot));
        predicates.add(buildingPredicateForSearchByEnd(rangeEnd, cb, eventRoot));
        Predicate[] param = new Predicate[predicates.size()];
        for (int i = 0; i < predicates.size(); i++) {
            param[i] = predicates.get(i);
        }
        cq.select(eventRoot).where(param);
        TypedQuery<Event> query = entityManager.createQuery(cq);
        List<Event> results = query.setMaxResults(size).setFirstResult(from).getResultList();
        log.info("Найдены все события соответствующие заданным фильтрам");
        return results;
    }

    @Override
    public List<Event> getEventsForUser(long userId, int from, int size) {
        commonMethods.checkExistUser(userId);
        Pageable pageable = Page.getPageable(from, size);
        List<Event> events = eventStorage.findByInitiatorId(userId, pageable);
        log.info("найдены все события добавленные пользователес с id {}", userId);
        return events;
    }

    @Override
    @Transactional
    public Event updateEventForUser(long userId, UpdateEventRequest updateEventRequest) {
        commonMethods.checkExistUser(userId);
        Event event = checkConditionForUpdate(userId, updateEventRequest);
        checkUserIdAndOwnerEvent(userId, event.getInitiator().getId());
        Event updatedEvent = eventStorage.save(updateEvent(event, updateEventRequest));

        log.info("Обновили событие с id " + updateEventRequest.getEventId());
        return updatedEvent;
    }

    @Override
    public Event getFullInfoEventForUser(long userId, long eventId) {
        commonMethods.checkExistUser(userId);
        Event event = commonMethods.checkExistEvent(eventId);
        checkUserIdAndOwnerEvent(userId, event.getInitiator().getId());
        log.info("Событие с id {} найдено", eventId);
        return event;
    }

    @Override
    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        commonMethods.checkExistUser(userId);
        Event event = commonMethods.checkExistEvent(eventId);
        checkUserIdAndOwnerEvent(userId, event.getInitiator().getId());
        if (!(event.getState().equals(EventState.PENDING.toString()))) {
            throw new NoRightsException("Отменить событие может только администратор или пользователь" +
                    "создавший данное событие");
        }
        event.setState(EventState.CANCELED.toString());
        Event canceledEvent = eventStorage.save(event);
        log.info("Пользователь с id {} отменил событие с id {}", userId, event);
        return canceledEvent;
    }

    @Override
    public List<ParticipationRequest> getRequestsInfoInEvents(long userId, long eventId) {
        commonMethods.checkExistUser(userId);
        Event event = commonMethods.checkExistEvent(eventId);
        checkUserIdAndOwnerEvent(userId, event.getInitiator().getId());
        List<ParticipationRequest> requestList = requestStorage.findByEventId(eventId);
        log.info("Запросы на участие в событии с id {} найдены", event);
        return requestList;
    }

    @Override
    @Transactional
    public ParticipationRequest confirmRequestInEvents(long userId, long eventId, long reqId) {
        commonMethods.checkExistUser(userId);
        Event event = commonMethods.checkExistEvent(eventId);
        ParticipationRequest request = commonMethods.checkExistRequest(reqId);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventStorage.save(event);
        log.info("Обновлено количество одобренных заявок на участие в событии с id {}", eventId);
        request.setStatus(RequestStatus.CONFIRMED.toString());
        ParticipationRequest confirmRequest = requestStorage.save(request);
        log.info("Заявка на участие с id {} одобрена", reqId);
        return confirmRequest;
    }

    @Override
    @Transactional
    public ParticipationRequest rejectRequestInEvents(long userId, long eventId, long reqId) {
        commonMethods.checkExistUser(userId);
        commonMethods.checkExistEvent(eventId);
        ParticipationRequest request = commonMethods.checkExistRequest(reqId);
        request.setStatus(RequestStatus.REJECTED.toString());
        ParticipationRequest confirmRequest = requestStorage.save(request);
        log.info("Заявка на участие с id {} отклонена", reqId);
        return confirmRequest;
    }

    @Override
    @Transactional
    public Event redactionEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = commonMethods.checkExistEvent(eventId);
        Event redactedEvent = eventStorage.save(updateEvent(event, adminUpdateEventRequest));
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
            log.info("Обновили локацию события");
        }
        log.info("Событие с id {} отредактировано", event);
        return redactedEvent;
    }

    @Override
    @Transactional
    public Event publishEvent(long eventId) {
        Event event = commonMethods.checkExistEvent(eventId);
        checkDateTime(LocalDateTime.now(), event);
        if (!(event.getState().equals(EventState.PENDING.toString()))) {
            throw new UnavailableException("Событие не ожидает публикации");
        }
        event.setState(EventState.PUBLISHED.toString());
        Event publishedEvent = eventStorage.save(event);
        log.info("Событие с id {} опубликовано", eventId);
        return publishedEvent;
    }

    @Override
    @Transactional
    public Event rejectedEvent(long eventId) {
        Event event = commonMethods.checkExistEvent(eventId);
        if (event.getState().equals(EventState.PUBLISHED.toString())) {
            throw new UnavailableException("Событие уже опубликовано");
        }
        event.setState(EventState.CANCELED.toString());
        Event publishedEvent = eventStorage.save(event);
        log.info("Событие с id {} отклонено", eventId);
        return publishedEvent;
    }

    private Predicate buildingPredicateForSearchByStart(String rangeStart,
                                                        CriteriaBuilder cb,
                                                        Root<Event> eventRoot) {
        Predicate predicate;
        if (!(rangeStart.isEmpty())) {
            predicate = cb.greaterThan(eventRoot.get("eventDate"),
                    cb.literal(commonMethods.toLocalDataTime(rangeStart)));
        } else {
            predicate = cb.greaterThan(eventRoot.get("eventDate"),
                    cb.literal(LocalDateTime.now()));
        }
        return predicate;
    }

    private Predicate buildingPredicateForSearchByEnd(String rangeEnd,
                                                      CriteriaBuilder cb,
                                                      Root<Event> eventRoot) {
        Predicate predicate;
        if (!(rangeEnd.isEmpty())) {
            predicate = cb.lessThan(eventRoot.get("eventDate"),
                    cb.literal(commonMethods.toLocalDataTime(rangeEnd)));
        } else {
            predicate = cb.lessThan(eventRoot.get("eventDate"),
                    cb.literal(LocalDateTime.now()));
        }
        return predicate;
    }

    private Event updateEvent(Event oldEvent, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime eventDate = commonMethods.toLocalDataTime(updateEventRequest.getEventDate());
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

    private Event checkConditionForUpdate(long userId, UpdateEventRequest updateEventRequest) {
        Event event = eventStorage.findById(updateEventRequest.getEventId()).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет события с id " + updateEventRequest.getEventId()));
        if (event.getState().equals(EventState.PUBLISHED.toString())) {
            throw new ConflictEventStatusException("Нельзя изменить опубликованное событие");
        }
        if (event.getInitiator().getId() != userId) {
            throw new NoRightsException("Изменить событие можеть администратор или" +
                    " пользователь создавший данное событие");
        }
        return event;
    }

    private void checkDateTime(LocalDateTime start, Event event) {
        if (start.isAfter(event.getEventDate())) {
            throw new ConflictDataTimeException("Время старта события в прошлом");
        }
        Duration duration = Duration.between(start, event.getEventDate());
        if (duration.toHours() < 2) {
            throw new ConflictDataTimeException("Время старта события раньше, чем 2 часа от текущего времени");
        }
    }

    private void addField(Long userId, Event event) {
        User user = commonMethods.checkExistUser(userId);
        event.setInitiator(user);
        event.setConfirmedRequests(0L);
        LocalDateTime start = LocalDateTime.now();
        checkDateTime(start, event);
        event.setCreatedOn(start);
        event.setState(EventState.PENDING.toString());
        event.setViews(0L);
    }

    private void checkUserIdAndOwnerEvent(long userId, long initiatorId) {
        if (initiatorId != userId) {
            throw new NoRightsException("Данное событие добавил другой пользователь");
        }
    }

    private void sendToStatServer(HttpServletRequest request) {
        NewEndpointHitDto newEndpointHitDto = new NewEndpointHitDto();
        newEndpointHitDto.setIp(request.getRemoteAddr());
        newEndpointHitDto.setUri(request.getRequestURI());
        newEndpointHitDto.setApp("ewm-main-service");
        newEndpointHitDto.setTimestamp(commonMethods.toString(LocalDateTime.now()));
        client.addEndpoint(newEndpointHitDto);
    }
}
