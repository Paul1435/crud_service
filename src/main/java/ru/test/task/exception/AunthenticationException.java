package ru.test.task.exception;

public class AunthenticationException extends RuntimeException {

    public AunthenticationException() {
    }

    public AunthenticationException(String message) {
        super(message);
    }

    public AunthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
