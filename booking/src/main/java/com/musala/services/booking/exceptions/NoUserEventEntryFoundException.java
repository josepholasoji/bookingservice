package com.musala.services.booking.exceptions;

public class NoUserEventEntryFoundException extends RuntimeException {
    public NoUserEventEntryFoundException(String message) {
        super(message);
    }
}
