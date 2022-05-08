package com.example.conferences.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Lecture is full")
public class LectureFullException extends RuntimeException {}
