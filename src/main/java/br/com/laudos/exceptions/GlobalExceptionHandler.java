package br.com.laudos.exceptions;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "Erro: ";

    @ExceptionHandler(LaudoServiceException.class)
    public ResponseEntity<String> handleLaudoServiceException(LaudoServiceException e) {
        return new ResponseEntity<>(ERROR.concat(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(ERROR.concat(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handlerBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(ERROR.concat(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerRuntimeException(ParseException e) {
        return new ResponseEntity<>(ERROR.concat(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    public static class LaudoServiceException extends RuntimeException {
        public LaudoServiceException(String message) {
            super(message);
        }
        public LaudoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
