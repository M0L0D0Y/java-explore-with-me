package ru.practicum.ewm.subscription;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.exception.UnavailableException;
import ru.practicum.ewm.participationRequest.ParticipationRequestStorage;
import ru.practicum.ewm.participationRequest.RequestStatus;
import ru.practicum.ewm.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService {
    private final CommonMethods commonMethods;
    private final ParticipationRequestStorage participationRequestStorage;

    @Autowired
    public SubscriptionServiceImpl(CommonMethods commonMethods,
                                   ParticipationRequestStorage participationRequestStorage) {
        this.commonMethods = commonMethods;
        this.participationRequestStorage = participationRequestStorage;
    }

    @Override
    @Transactional
    public void addFriend(long userId, long friendId) {
        User user = commonMethods.checkExistUser(userId);
        User friend = commonMethods.checkExistUser(friendId);
        Set<User> listFriends = user.getFriends();
        if (listFriends.contains(friend)) {
            throw new UnavailableException("Нельзя повторно подписаться на одного и того же пользователя");
        }
        listFriends.add(friend);
        log.info("Пользователь с id {} подписался на пользователя с id {}", userId, friendId);
    }

    @Override
    @Transactional
    public void deleteFriend(long userId, long friendId) {
        User user = commonMethods.checkExistUser(userId);
        User friend = commonMethods.checkExistUser(friendId);
        Set<User> listFriends = user.getFriends();
        if (!(listFriends.contains(friend))) {
            throw new UnavailableException("Нельзя удалить пользователя, на которого вы не подписаны");
        }
        listFriends.remove(friend);
        log.info("Пользователь с id {} отписался от пользователя с id {}", userId, friendId);
    }

    @Override
    public Set<User> getFriends(long userId) {
        User user = commonMethods.checkExistUser(userId);
        log.info("Нашли пользователя с id {}", userId);
        Set<User> friends = user.getFriends();
        log.info("Получили список пользователей на которых подписан пользователь с id {}", userId);
        return friends;
    }

    @Override
    @Transactional
    public List<Event> getEventsByFriendId(long userId, long friendId, boolean isCommonFriend) {
        User user = commonMethods.checkExistUser(userId);
        User friend = commonMethods.checkExistUser(friendId);
        Set<User> listFriends = user.getFriends();
        if (!(listFriends.contains(friend))) {
            throw new UnavailableException("Нельзя получить список событий пользователя, на которого вы не подписаны");
        }
        List<Event> events;
        if (!isCommonFriend) {
            events = participationRequestStorage
                    .findEventsByFriendId(friendId, RequestStatus.CONFIRMED.toString());
            log.info("Получены все события в которых участвует пользователь с id {}", friendId);
        } else {
            events = participationRequestStorage
                    .findCommonEventsWithFriend(userId, friendId, RequestStatus.CONFIRMED.toString());
            log.info("Получены все общие события");
        }
        return events;
    }

    @Override
    @Transactional
    public List<Event> getEventsAllFriends(long userId) {
        User user = commonMethods.checkExistUser(userId);
        Set<User> listFriends = user.getFriends();
        if (listFriends.isEmpty()) {
            throw new UnavailableException("Вы ни на кого не подписаны");
        }
        List<Long> idFriends = new ArrayList<>();
        for (User u : listFriends) {
            idFriends.add(u.getId());
        }
        List<Event> events = participationRequestStorage
                .findEventsAllFriends(RequestStatus.CONFIRMED.toString(), idFriends);
        log.info("Получены все события пользователей на которых вы подписаны");
        return events;
    }
}
