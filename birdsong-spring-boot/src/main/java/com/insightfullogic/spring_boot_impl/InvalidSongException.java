package com.insightfullogic.spring_boot_impl;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * .
 */
@ResponseStatus(BAD_REQUEST)
public class InvalidSongException extends IllegalStateException {
    public InvalidSongException(Throwable e) {
        super(e);
    }

    public InvalidSongException() {

    }
}
