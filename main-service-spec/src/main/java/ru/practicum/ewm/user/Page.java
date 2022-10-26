package ru.practicum.ewm.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.ecxeption.UnavailableException;

public class Page {
    public static Pageable getPageable(int from, int size) {
        if ((from < 0) || (size < 1)) {
            throw new UnavailableException("неправильно заданы параметры запроса " +
                    "индекс " + from + " количество элементов " + size);
        }
        int page;
        if (from == 0) {
            page = 0;
        } else {
            page = size / from;
        }
        return PageRequest.of(page, size);
    }
}
