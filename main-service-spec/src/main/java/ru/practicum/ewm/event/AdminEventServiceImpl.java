package ru.practicum.ewm.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.exception.UnavailableException;

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
public class AdminEventServiceImpl implements AdminEventService {
    private final EntityManager entityManager;
    private final EventStorage eventStorage;
    private final CommonMethods commonMethods;

    public AdminEventServiceImpl(EntityManager entityManager,
                                 EventStorage eventStorage,
                                 CommonMethods commonMethods) {
        this.entityManager = entityManager;
        this.eventStorage = eventStorage;
        this.commonMethods = commonMethods;
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
        predicates.add(commonMethods.buildingPredicateForSearchByStart(rangeStart, cb, eventRoot));
        predicates.add(commonMethods.buildingPredicateForSearchByEnd(rangeEnd, cb, eventRoot));
        Predicate[] param = new Predicate[predicates.size()];
        for (int i = 0; i < predicates.size(); i++) {
            param[i] = predicates.get(i);
        }
        cq.select(eventRoot).where(param);
        TypedQuery<Event> query = entityManager.createQuery(cq);
        List<Event> results = query.setMaxResults(size).setFirstResult(from).getResultList();
        log.info("?????????????? ?????? ?????????????? ?????????????????????????????? ???????????????? ????????????????");
        return results;
    }

    @Override
    @Transactional
    public Event redactionEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = commonMethods.checkExistEvent(eventId);
        Event redactedEvent = eventStorage.save(commonMethods.updateEvent(event, adminUpdateEventRequest));
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
            log.info("???????????????? ?????????????? ??????????????");
        }
        log.info("?????????????? ?? id {} ??????????????????????????????", event);
        return redactedEvent;
    }

    @Override
    @Transactional
    public Event publishEvent(long eventId) {
        Event event = commonMethods.checkExistEvent(eventId);
        commonMethods.checkDateTime(LocalDateTime.now(), event);
        if (!(event.getState().equals(EventState.PENDING.toString()))) {
            throw new UnavailableException("?????????????? ???? ?????????????? ????????????????????");
        }
        event.setState(EventState.PUBLISHED.toString());
        event.setPublishedOn(LocalDateTime.now());
        Event publishedEvent = eventStorage.save(event);
        log.info("?????????????? ?? id {} ????????????????????????", eventId);
        return publishedEvent;
    }

    @Override
    @Transactional
    public Event rejectedEvent(long eventId) {
        Event event = commonMethods.checkExistEvent(eventId);
        if (event.getState().equals(EventState.PUBLISHED.toString())) {
            throw new UnavailableException("?????????????? ?????? ????????????????????????");
        }
        event.setState(EventState.CANCELED.toString());
        Event publishedEvent = eventStorage.save(event);
        log.info("?????????????? ?? id {} ??????????????????", eventId);
        return publishedEvent;
    }
}
