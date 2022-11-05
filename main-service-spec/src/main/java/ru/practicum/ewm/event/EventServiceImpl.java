package ru.practicum.ewm.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.exception.ConflictEventStatusException;
import ru.practicum.ewm.exception.NoRightsException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.UnavailableException;
import ru.practicum.ewm.participationRequest.ParticipationRequest;
import ru.practicum.ewm.participationRequest.ParticipationRequestStorage;
import ru.practicum.ewm.participationRequest.RequestStatus;
import ru.practicum.ewm.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EntityManager entityManager;
    private final EventStorage eventStorage;

    private final ParticipationRequestStorage requestStorage;
    private final CommonMethods commonMethods;

    @Autowired
    public EventServiceImpl(EntityManager entityManager, EventStorage eventStorage,
                            ParticipationRequestStorage requestStorage,
                            CommonMethods commonMethods) {
        this.entityManager = entityManager;
        this.eventStorage = eventStorage;
        this.requestStorage = requestStorage;
        this.commonMethods = commonMethods;
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
    public Event getEvent(long id) {
        Event event = commonMethods.checkExistEvent(id);
        if (!(event.getState().equals(EventState.PUBLISHED.toString()))) {
            throw new UnavailableException("Событие не опубликовано");
        }
        return event;
    }

    @Override
    public List<Event> getEvents(String text, List<Integer> categories, Boolean paid,
                                 String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                 String sort, Integer from, Integer size) {

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
        predicates.add(cb.equal(eventRoot.get("state"), EventState.PUBLISHED.toString()));
        predicates.add(commonMethods.buildingPredicateForSearchByStart(rangeStart, cb, eventRoot));
        predicates.add(commonMethods.buildingPredicateForSearchByEnd(rangeEnd, cb, eventRoot));
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
        return results;
    }


    @Override
    public List<Event> getEventsForUser(long userId, int from, int size) {
        commonMethods.checkExistUser(userId);
        Pageable pageable = commonMethods.getPageable(from, size);
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
        Event updatedEvent = eventStorage.save(commonMethods.updateEvent(event, updateEventRequest));

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

    private void addField(Long userId, Event event) {
        User user = commonMethods.checkExistUser(userId);
        event.setInitiator(user);
        event.setConfirmedRequests(0L);
        LocalDateTime start = LocalDateTime.now();
        commonMethods.checkDateTime(start, event);
        event.setCreatedOn(start);
        event.setState(EventState.PENDING.toString());
    }

    private void checkUserIdAndOwnerEvent(long userId, long initiatorId) {
        if (initiatorId != userId) {
            throw new NoRightsException("Данное событие добавил другой пользователь");
        }
    }
}
