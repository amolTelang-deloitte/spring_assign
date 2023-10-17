package com.imbd.imbd.exception;

import com.imbd.imbd.exception.customExceptions.InvalidRatingException;
import com.imbd.imbd.exception.customExceptions.InvalidUserException;
import com.sun.media.sound.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InvalidUserException.class)
    public ResponseEntity<?> handleInvalidUserException(InvalidUserException ex){
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

    @ExceptionHandler(value = InvalidRatingException.class)
    public ResponseEntity<?>  handleInvalidRating(InvalidRatingException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

}
