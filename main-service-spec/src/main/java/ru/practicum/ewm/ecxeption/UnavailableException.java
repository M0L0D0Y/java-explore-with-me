package ru.practicum.ewm.ecxeption;

public class UnavailableException extends RuntimeException {
    public UnavailableException(String message) {
        super(message);
    }
}
