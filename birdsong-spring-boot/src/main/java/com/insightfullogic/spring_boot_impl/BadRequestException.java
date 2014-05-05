package com.insightfullogic.spring_boot_impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * .
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(Throwable e) {
        super(e);
    }
}
