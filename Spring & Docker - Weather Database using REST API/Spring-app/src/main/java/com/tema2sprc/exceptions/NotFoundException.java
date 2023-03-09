package com.tema2sprc.exceptions;

import org.springframework.stereotype.Component;

@Component
public class NotFoundException extends RuntimeException {
    public NotFoundException() {super("[WARNING]: Elementul nu exista in baza de date");}
}
