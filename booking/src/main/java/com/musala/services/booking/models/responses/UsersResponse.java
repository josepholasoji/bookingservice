package com.musala.services.booking.models.responses;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.musala.services.booking.configuration.ResponseDeserializer;
import com.musala.services.booking.models.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersResponse extends Response {
    @JsonDeserialize(using = ResponseDeserializer.class)
    private final List<User> data;

    public UsersResponse(List<User> events) {
        this.data = events;
    }

    @JsonProperty
    public List<User> getData() {
        return data;
    }
}
