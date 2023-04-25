package pro.sky.sockswarehouse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pro.sky.sockswarehouse.service.SocksService;


@RestControllerAdvice
public class ExceptionApiHandler {

    private static final Logger log = LoggerFactory.getLogger(SocksService.class);

//    // Исключение в виде строки без тела
//    @ExceptionHandler(IllegalArgumentException.class)
//    public final ResponseEntity<String> handleInvalidValueException(IllegalArgumentException exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorMessage> handleInvalidValueException(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(exception.getMessage()));
    }
}
