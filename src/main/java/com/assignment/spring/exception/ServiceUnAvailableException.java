package com.assignment.spring.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceUnAvailableException extends RuntimeException {

    public ServiceUnAvailableException(String message) {
        super(message);
    }
}
