package ru.practicum.mainservice.ecxeption;

public class UnavailableException extends RuntimeException {
    public UnavailableException(String message) {
        super(message);
    }
}
