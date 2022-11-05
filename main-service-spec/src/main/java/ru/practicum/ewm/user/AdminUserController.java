package ru.practicum.ewm.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.common.Create;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class AdminUserController {
    private final AdminUserService adminUserService;
    private final UserMapper userMapper;
    private final CommonMethods commonMethods;

    @Autowired
    public AdminUserController(AdminUserService adminUserService,
                               UserMapper userMapper,
                               CommonMethods commonMethods) {
        this.adminUserService = adminUserService;
        this.userMapper = userMapper;
        this.commonMethods = commonMethods;
    }


    @GetMapping(value = "/admin/users")
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<UserDto> userDtos = findUsers(ids, from, size);
        log.info("{}", userDtos);
        return userDtos;
    }

    @PostMapping(value = "/admin/users")
    public UserDto addUser(@RequestBody @Validated(Create.class) NewUserRequest newUserRequest) {
        User user = userMapper.toUser(newUserRequest);
        UserDto userDto = userMapper.toUserDto(adminUserService.addUser(user));
        log.info("{}", userDto);
        return userDto;
    }

    @DeleteMapping(value = "/admin/users/{userId}")
    public void deleteUser(@PathVariable @Positive Long userId) {
        adminUserService.deleteUser(userId);
    }

    private List<UserDto> findUsers(List<Long> ids, int from, int size) {
        if (ids != null) {
            return adminUserService.getAllUsersForList(ids)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            Pageable pageable = commonMethods.getPageable(from, size);
            return adminUserService.getAllUsers(pageable)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }
}
