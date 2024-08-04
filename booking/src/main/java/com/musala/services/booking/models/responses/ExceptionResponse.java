package com.musala.services.booking.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse extends Response {

    private final String data;

    public ExceptionResponse() {
        this.data = null;
    }

    public ExceptionResponse(int status, String message) {
        super(status, "Failed");
        this.data = message;
    }

    public String getData() {
        return data;
    }

}
