package ru.practicum.ewm.subscription;

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

    @PostMapping(value = "/{friendId}")
    public void addFriend(@PathVariable @NotNull @Positive Long userId,
                          @PathVariable @NotNull @Positive Long friendId) {
        friendService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/{friendId}")
    public void deleteFriend(@PathVariable @NotNull @Positive Long userId,
                             @PathVariable @NotNull @Positive Long friendId) {
        friendService.deleteFriend(userId, friendId);
    }

    @GetMapping()
    public List<UserDto> getFriends(@PathVariable @NotNull @Positive Long userId) {
        Set<User> users = friendService.getFriends(userId);
        return users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/{friendId}/events")
    public List<EventShortDto> getEventsFriend(@PathVariable @NotNull @Positive Long userId,
                                               @PathVariable @NotNull @Positive Long friendId,
                                               @RequestParam(defaultValue = "false") @NotNull Boolean isCommonFriend) {
        List<Event> events = friendService.getEventsByFriendId(userId, friendId, isCommonFriend);
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/events")
    public List<EventShortDto> getEventsAllFriends(@PathVariable @NotNull @Positive Long userId) {
        List<Event> events = friendService.getEventsAllFriends(userId);
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }
}

