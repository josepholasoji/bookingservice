package com.musala.services.booking.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.musala.services.booking.configuration.ResponseDeserializer;
import com.musala.services.booking.models.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends Response{

    @JsonDeserialize(using = ResponseDeserializer.class)
    private final User data;

    public UserResponse(int id, String name, String email, String role) {
        this.data = new User(id, name, email, null, role);
    }

    public UserResponse(User user) {
        this.data = user;
    }

    @JsonProperty
    public User getData() {
        return data;
    }
}
