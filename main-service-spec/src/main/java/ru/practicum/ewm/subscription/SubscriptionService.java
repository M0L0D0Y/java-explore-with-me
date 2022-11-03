package ru.practicum.ewm.subscription;


import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.User;

import java.util.List;
import java.util.Set;

public interface SubscriptionService {
    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Set<User> getFriends(long userId);

    List<Event> getEventsByFriendId(long userId, long friendId, boolean isCommonFriend);

    List<Event> getEventsAllFriends(long userId);
}
