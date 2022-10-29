package ru.practicum.ewm.feature;


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
import ru.practicum.ewm.user.UserStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class FriendServiceImpl implements FriendService {
    private final UserStorage userStorage;
    private final CommonMethods commonMethods;
    private final ParticipationRequestStorage participationRequestStorage;

    @Autowired
    public FriendServiceImpl(UserStorage userStorage,
                             CommonMethods commonMethods,
                             ParticipationRequestStorage participationRequestStorage) {
        this.userStorage = userStorage;
        this.commonMethods = commonMethods;
        this.participationRequestStorage = participationRequestStorage;
    }

    //TODO подумать что будет возвращать этот метод
    @Override
    @Transactional
    public void addFriend(long userId, long friendId) {
        User user = commonMethods.checkExistUser(userId);
        User friend = commonMethods.checkExistUser(friendId);
        Set<User> listFriends = user.getFriends();
        if (listFriends.contains(friend)) {
            throw new UnavailableException("Нельзя повторно добавить одно и того же пользователя");
        }
        listFriends.add(friend);
        user.setFriends(listFriends);
        userStorage.save(user);
        log.info("Пользователь с id {} добавил в друзья пользователя с id {}", userId, friendId);
    }

    @Override
    @Transactional
    public void deleteFriend(long userId, long friendId) {
        User user = commonMethods.checkExistUser(userId);
        User friend = commonMethods.checkExistUser(friendId);
        Set<User> listFriends = user.getFriends();
        if (!(listFriends.contains(friend))) {
            throw new UnavailableException("Нельзя удалить пользователя, которого нет в списке друзей");
        }
        listFriends.remove(friend);
        user.setFriends(listFriends);
        userStorage.save(user);
        log.info("Пользователь с id {} удалил из друзей пользователя с id {}", userId, friendId);
    }

    @Override
    public Set<User> getFriends(long userId) {
        User user = commonMethods.checkExistUser(userId);
        log.info("Получили список друзей пользователя с id {}", userId);
        return user.getFriends();
    }

    @Override
    public List<Event> getEventsByFriendId(long userId, long friendId, boolean common) {
        User user = commonMethods.checkExistUser(userId);
        User friend = commonMethods.checkExistUser(friendId);
        Set<User> listFriends = user.getFriends();
        if (!(listFriends.contains(friend))) {
            throw new UnavailableException("Нельзя получить список событий пользователя, которого нет в списке друзей");
        }
        List<Event> events;
        if (!common) {
            events = participationRequestStorage
                    .findEventsByFriendId(friendId, RequestStatus.CONFIRMED.toString());
            log.info("Получены все события в которых участвует пользователь с id {}", friendId);
        } else {
            events = participationRequestStorage
                    .findCommonEventsWithFriend(userId, friendId, RequestStatus.CONFIRMED.toString());
            log.info("Получены все события все общие события");
        }
        return events;
    }

    //можно добавить Pageable
    @Override
    public List<Event> getEventsAllFriends(long userId) {
        User user = commonMethods.checkExistUser(userId);
        Set<User> listFriends = user.getFriends();
        if (listFriends.isEmpty()) {
            throw new UnavailableException("Вы ни на кого не подписаны");
        }
        List<Long> idFriends = new LinkedList<>();
        for (User u : listFriends) {
            idFriends.add(u.getId());
        }
        List<Event> events = participationRequestStorage
                .findEventsAllFriends(RequestStatus.CONFIRMED.toString(), idFriends);
        log.info("Получены все события в которых участвуют ваши друзья");
        return events;
    }
    //TODO Добавить функцию, для просмотра друзей идущих на теже события что и пользователь,
    // внедрить через параметр в контроллере для эндпоинтов getEventsFriend и getEventsAllFriends
    // может быть имеет смысл добавить параметр на добавление в друзья,
    // аля статус дружбы -заявка в друзья в ожидании, подтверждена, отклонена/отменена
    // если статус в ожидании то можно увидеть пользователя в списке друзей, но нельзя увидеть его ивенты
    // если статус подтверждена то можно увидеть пользователя в списке друзей и список его ивентов, тоже самое может увидеть и он
    // если стутус отклонена то нельзя увидеть пользователя в списке друзей и нельзя увидеть его ивенты.
}
