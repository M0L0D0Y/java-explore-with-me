package ru.practicum.ewm.ecxeption;

public class NoRightsException extends RuntimeException {
    public NoRightsException(String message) {
        super(message);
    }
}
