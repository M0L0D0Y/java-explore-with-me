package ru.practicum.ewm.ecxeption;

public class ConflictDataTimeException extends RuntimeException {
    public ConflictDataTimeException(String message) {
        super(message);
    }
}
