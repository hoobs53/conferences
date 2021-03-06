package com.example.conferences.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Użytkownik ma już prelekcję o tej godzinie")
public class UserNotFreeException extends RuntimeException {}
