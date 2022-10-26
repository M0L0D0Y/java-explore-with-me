package ru.practicum.ewm.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.ecxeption.NotFoundException;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventStorage;
import ru.practicum.ewm.category.CategoryStorage;
import ru.practicum.ewm.participationRequest.ParticipationRequest;
import ru.practicum.ewm.participationRequest.ParticipationRequestStorage;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CommonMethods {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat(PATTERN);
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final ParticipationRequestStorage requestStorage;
    private final CategoryStorage categoryStorage;

    @Autowired
    public CommonMethods(UserStorage userStorage,
                         EventStorage eventStorage,
                         ParticipationRequestStorage requestStorage,
                         CategoryStorage categoryStorage) {
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
        this.requestStorage = requestStorage;
        this.categoryStorage = categoryStorage;
    }

    public LocalDateTime toLocalDataTime(String dateTime) {
        try {
            Date date = sdf.parse(dateTime);
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(LocalDateTime localDateTime) {
        Date dateToConvert = java.sql.Timestamp.valueOf(localDateTime);
        try {
            Date date = sdf.parse(sdf.format(dateToConvert));
            return sdf.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public User checkExistUser(long id) {
        return userStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id {}" + id));
    }

    public Event checkExistEvent(long id) {
        return eventStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет события с таким id " + id));
    }

    public ParticipationRequest checkExistRequest(long id) {
        return requestStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет заявки с таким id " + id));
    }

    public Category checkExistCategory(long id) {
        return categoryStorage.findById(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Категории с таким id нет " + id));
    }
}
