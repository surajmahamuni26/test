package com.globomantics.rewards.interceptors;

import com.globomantics.rewards.controller.RewardsController;
import com.globomantics.rewards.models.exceptions.BadRequest;
import com.globomantics.rewards.models.exceptions.RewardsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {RewardsController.class})
public class RewardsControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardsControllerAdvice.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<Object> handleBadRequest(BadRequest badRequest) {
        return new ResponseEntity<>(new RewardsException(badRequest.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleInternalServerError(Throwable e) {
        LOGGER.error("Internal Server Error", e);
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
