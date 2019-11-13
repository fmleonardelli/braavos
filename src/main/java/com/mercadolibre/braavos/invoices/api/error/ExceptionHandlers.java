package com.mercadolibre.braavos.invoices.api.error;

import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlers {

    public static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(ValidationError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> validationErrorException(ValidationError ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<ApiError>(new ApiError(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> notFoundException(NotFound ex) {
        return new ResponseEntity<ApiError>(new ApiError(HttpStatus.NOT_FOUND.toString(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> generalException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<ApiError>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
