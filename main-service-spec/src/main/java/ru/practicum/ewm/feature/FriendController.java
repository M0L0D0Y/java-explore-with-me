package ru.practicum.ewm.feature;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class FriendController {
    private final FriendService friendService;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Autowired
    public FriendController(FriendService friendService, EventMapper eventMapper, UserMapper userMapper) {
        this.friendService = friendService;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/users/{userId}/friends/{friendId}")
    public String addFriend(@PathVariable @NotNull @Positive Long userId,
                            @PathVariable @NotNull @Positive Long friendId) {
        friendService.addFriend(userId, friendId);
        return "Пользователь добавлен в друзья";
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public String deleteFriend(@PathVariable @NotNull @Positive Long userId,
                               @PathVariable @NotNull @Positive Long friendId) {
        friendService.deleteFriend(userId, friendId);
        return "Пользователь удален из в друзей";
    }

    @GetMapping(value = "/users/{userId}/friends")
    public List<UserDto> getFriends(@PathVariable @NotNull @Positive Long userId) {
        Set<User> users = friendService.getFriends(userId);
        return users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/users/{userId}/friends/{friendId}/events")
    public List<EventShortDto> getEventsFriend(@PathVariable @NotNull @Positive Long userId,
                                               @PathVariable @NotNull @Positive Long friendId,
                                               @RequestParam(defaultValue = "false") @NotNull Boolean common) {
        List<Event> events = friendService.getEventsByFriendId(userId, friendId, common);
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/users/{userId}/friends/events")
    public List<EventShortDto> getEventsAllFriends(@PathVariable @NotNull @Positive Long userId) {
        List<Event> events = friendService.getEventsAllFriends(userId);
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }
}

