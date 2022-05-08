package com.example.conferences.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Podany login jest już zajęty")
public class LoginTakenException extends RuntimeException {}
