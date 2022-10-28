package ru.practicum.ewm.participationRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.UnavailableException;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.exception.NoRightsException;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.event.EventStorage;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestStorage storage;
    private final EventStorage eventStorage;
    private final CommonMethods commonMethods;

    @Autowired
    public ParticipationRequestServiceImpl(ParticipationRequestStorage storage,
                                           EventStorage eventStorage,
                                           CommonMethods commonMethods) {
        this.storage = storage;
        this.eventStorage = eventStorage;
        this.commonMethods = commonMethods;
    }

    @Override
    @Transactional
    public ParticipationRequest addRequestForEvent(long userId, long eventId) {
        User user = commonMethods.checkExistUser(userId);
        Event event = commonMethods.checkExistEvent(eventId);
        checkConditionCreate(userId, event);
        ParticipationRequest newRequest = new ParticipationRequest();
        newRequest.setEvent(event);
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now());
        if (!(event.getRequestModeration())) {
            newRequest.setStatus(RequestStatus.CONFIRMED.toString());
        } else {
            newRequest.setStatus(RequestStatus.PENDING.toString());
        }
        ParticipationRequest savedRequest = storage.save(newRequest);
        log.info("Запрос на участие в событии с id {} пользователя с id {} создан", eventId, userId);
        return savedRequest;
    }

    @Override
    public List<ParticipationRequest> getRequestInfoForUser(long userId) {
        commonMethods.checkExistUser(userId);
        List<ParticipationRequest> requestList = storage.findByRequesterId(userId);
        log.info("Найдены все заявки пользователя с id {}", userId);
        return requestList;
    }

    @Override
    @Transactional
    public ParticipationRequest cancelRequestForEvent(long userId, long requestsId) {
        commonMethods.checkExistUser(userId);
        ParticipationRequest request = commonMethods.checkExistRequest(requestsId);
        checkConditionCanceledRequest(userId, request);
        if (request.getStatus().equals(RequestStatus.CONFIRMED.toString())) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventStorage.save(event);
            request.setStatus(RequestStatus.CANCELED.toString());
        }
        if (request.getStatus().equals(RequestStatus.PENDING.toString())) {
            request.setStatus(RequestStatus.CANCELED.toString());
        }
        ParticipationRequest updateRequest = storage.save(request);
        log.info("Заявка отменена");
        return updateRequest;

    }

    private void checkConditionCanceledRequest(long userId, ParticipationRequest request) {
        if (request.getRequester().getId() != userId) {
            throw new NoRightsException("Нельзя отменить заявку другого пользователя");
        }
        if (request.getStatus().equals(RequestStatus.REJECTED.toString())) {
            throw new UnavailableException("Заявка была отклонена создателем события");
        }
        if (request.getStatus().equals(RequestStatus.CANCELED.toString())) {
            throw new UnavailableException("Заявка была отменена вами ранее");
        }
    }

    private void checkConditionCreate(long userId, Event event) {
        if (userId == event.getInitiator().getId()) {
            throw new UnavailableException("Нельзя участвовать в своем мероприятии");
        }
        if (!(event.getState().equals(EventState.PUBLISHED.toString()))) {
            throw new UnavailableException("Нельзя участвовать в неопубликованом мероприятии");
        }
        long limit = event.getParticipantLimit();
        long confirm = event.getConfirmedRequests();
        if (limit == confirm) {
            throw new UnavailableException("Достигнут лимит участников");
        }
        ParticipationRequest request = storage.findByEventAndUserId(userId, event.getId())
                .stream()
                .findAny()
                .orElse(new ParticipationRequest());
        if (request.getId() != null) {
            throw new UnavailableException("Нельзя добавить повторный запрос");
        }
    }


}
