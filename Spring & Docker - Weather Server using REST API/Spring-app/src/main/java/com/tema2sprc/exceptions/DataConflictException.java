package com.tema2sprc.exceptions;

import org.springframework.stereotype.Component;

@Component
public class DataConflictException extends RuntimeException {
    public DataConflictException() {super("[WARNING]: Entitatea exista deja");}
}
