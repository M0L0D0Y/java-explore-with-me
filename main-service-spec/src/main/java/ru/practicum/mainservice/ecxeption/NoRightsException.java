package ru.practicum.mainservice.ecxeption;

public class NoRightsException extends RuntimeException {
    public NoRightsException(String message) {
        super(message);
    }
}
