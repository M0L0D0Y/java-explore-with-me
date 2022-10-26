package ru.practicum.ewm.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.ecxeption.NotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        User savedUser = userStorage.save(user);
        log.info("Пользователь сохранен");
        return savedUser;
    }

    @Override
    public List<User> getAllUsers(Pageable pageable) {
        return userStorage.findAllUsers(pageable);
    }

    @Override
    public List<User> getAllUsersForList(List<Long> ids) {
        return userStorage.findUserByList(ids);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + id));
        userStorage.deleteById(id);
        log.info("Пользоватеь с id = {} удален", id);
    }
}
