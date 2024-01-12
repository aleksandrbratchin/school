package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundElementException extends NoSuchElementException {
    public NotFoundElementException() {
    }

    public NotFoundElementException(String s) {
        super(s);
    }
}
