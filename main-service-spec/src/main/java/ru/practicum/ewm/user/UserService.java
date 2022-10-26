package ru.practicum.ewm.user;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User addUser(User user);

    List<User> getAllUsers(Pageable pageable);

    List<User> getAllUsersForList(List<Long> ids);

    void deleteUser(long id);
}
