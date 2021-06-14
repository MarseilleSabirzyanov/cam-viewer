package ru.kmpo.camviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class FileExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException e) {
        ResponseError err = getResponseError(e.getMessage(), "File Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException e) {
        ResponseError err = getResponseError(e.getMessage(), "File Size Exceeded");
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(err);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ResponseError err = getResponseError(e.getMessage(), "Resource Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException e) {
        ResponseError err = getResponseError(e.getMessage(), "Have No Access");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Object> handleForbiddenException(AlreadyExistException e) {
        ResponseError err = getResponseError(e.getMessage(), "Already Exist");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    private ResponseError getResponseError(String message, String s) {
        List<String> details = new ArrayList<>();
        details.add(message);
        return new ResponseError(LocalDateTime.now(), s, details);
    }
}
