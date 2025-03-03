package io.github.angel.raa.excpetion;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.FORBIDDEN)
public class LockedException extends RuntimeException {
    public LockedException(String message) {
        super(message);
    }
}
