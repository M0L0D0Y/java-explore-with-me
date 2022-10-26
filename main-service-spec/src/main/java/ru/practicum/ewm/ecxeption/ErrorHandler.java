package ru.practicum.ewm.ecxeption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.common.CommonMethods;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestControllerAdvice
public class ErrorHandler {
    private final CommonMethods commonMethods;

    @Autowired
    public ErrorHandler(CommonMethods commonMethods) {
        this.commonMethods = commonMethods;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Объект не найден",
                "NOT_FOUND", LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUnavailableException(final UnavailableException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Данный запрос не поддерживается",
                "BAD_REQUEST", commonMethods.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleNoRightsException(final NoRightsException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Нет прав",
                "FORBIDDEN", commonMethods.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleConflictDataTimeException(final ConflictDataTimeException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Недопустимое значение времени",
                "FORBIDDEN", commonMethods.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleConflictEventStatusException(final ConflictEventStatusException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Нельзя изменить опубликованное событие",
                "FORBIDDEN", commonMethods.toString(LocalDateTime.now()));
    }
}
