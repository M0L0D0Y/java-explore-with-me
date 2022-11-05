package ru.practicum.ewm.subscription;

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
@RequestMapping(path = "/users/{userId}/friends")
@Validated
@Slf4j
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, EventMapper eventMapper, UserMapper userMapper) {
        this.subscriptionService = subscriptionService;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/{friendId}")
    public void addFriend(@PathVariable @NotNull @Positive Long userId,
                          @PathVariable @NotNull @Positive Long friendId) {
        subscriptionService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/{friendId}")
    public void deleteFriend(@PathVariable @NotNull @Positive Long userId,
                             @PathVariable @NotNull @Positive Long friendId) {
        subscriptionService.deleteFriend(userId, friendId);
    }

    @GetMapping()
    public List<UserDto> getFriends(@PathVariable @NotNull @Positive Long userId) {
        Set<User> users = subscriptionService.getFriends(userId);
        List<UserDto> friends = users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
        log.info("{}", friends);
        return friends;
    }

    @GetMapping(value = "/{friendId}/events")
    public List<EventShortDto> getEventsFriend(@PathVariable @NotNull @Positive Long userId,
                                               @PathVariable @NotNull @Positive Long friendId,
                                               @RequestParam(defaultValue = "false") @NotNull Boolean isCommonFriend) {
        List<Event> events = subscriptionService.getEventsByFriendId(userId, friendId, isCommonFriend);
        List<EventShortDto> eventShortDtos = eventMapper.toListEventShortDto(events);
        log.info("{}", eventShortDtos);
        return eventShortDtos;
    }

    @GetMapping(value = "/events")
    public List<EventShortDto> getEventsAllFriends(@PathVariable @NotNull @Positive Long userId) {
        List<Event> events = subscriptionService.getEventsAllFriends(userId);
        List<EventShortDto> eventShortDtos = eventMapper.toListEventShortDto(events);
        log.info("{}", eventShortDtos);
        return eventShortDtos;
    }
}

