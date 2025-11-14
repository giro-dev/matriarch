package dev.agiro.matriarch.domain.exception;

public class MatriarchInstantiationException extends RuntimeException {
    public MatriarchInstantiationException(String message) {
        super(message);
    }

    public MatriarchInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
