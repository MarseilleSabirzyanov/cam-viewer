package ru.kmpo.camviewer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlreadyExistException extends RuntimeException{
    private String message;
}
