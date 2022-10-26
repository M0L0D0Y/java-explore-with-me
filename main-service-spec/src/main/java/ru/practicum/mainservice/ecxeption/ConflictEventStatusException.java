package ru.practicum.mainservice.ecxeption;

public class ConflictEventStatusException extends RuntimeException {
    public ConflictEventStatusException(String message) {
        super(message);
    }
}
