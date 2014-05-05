package com.insightfullogic.spring_boot_impl;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * .
 */
@ResponseStatus(FORBIDDEN)
public class ForbiddenException extends IllegalStateException {
    public ForbiddenException(Throwable e) {
        super(e);
    }

    public ForbiddenException() {

    }
}
