package ru.practicum.mainservice.ecxeption;

public class ConflictDataTimeException extends RuntimeException {
    public ConflictDataTimeException(String message) {
        super(message);
    }
}
