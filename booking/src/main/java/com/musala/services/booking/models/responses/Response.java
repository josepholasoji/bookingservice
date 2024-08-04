package com.musala.services.booking.models.responses;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {
    private int status = 200;
    private String message = "Success";

    public Response() {
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public int getStatus() {
        return status;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
