package com.assignment.spring.controller.handler;

import com.assignment.spring.exception.BadRequestException;
import com.assignment.spring.exception.NotFoundException;
import com.assignment.spring.exception.ServiceUnAvailableException;
import com.assignment.spring.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is5xxServerError()) {
            if (response.getStatusCode() == HttpStatus.NOT_IMPLEMENTED) {
                throw new ServiceUnAvailableException("Method is not implemented");
            }
            throw new ServiceUnAvailableException("Service is currently unavailable");
        }

        if (response.getStatusCode().is4xxClientError()) {
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UnAuthorizedException("Unauthorized access");
            }
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadRequestException("Bad request");
            }
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("City not found");
            }
        }
    }
}
