package com.assignment.spring.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
    }
}
