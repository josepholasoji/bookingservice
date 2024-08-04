package com.musala.services.booking.models.responses;

// Annoootation to serialize the object into JSON
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse extends Response{
    private final String data;

    public AuthenticationResponse(){
        super();
        this.data = null;
    }

    public AuthenticationResponse(String jwt) {
        super();
        this.data = jwt;
    }  

    public String getData() {
        return data;
    }
}
