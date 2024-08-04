package com.musala.services.booking.models.responses;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.musala.services.booking.configuration.ResponseDeserializer;
import com.musala.services.booking.models.Event;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsResponse extends Response {

    @JsonDeserialize(using = ResponseDeserializer.class)
    private List<Event> data;

    public EventsResponse() {
        super();
    }

    public EventsResponse(List<Event> events) {
        this.data = events;
    }

    @JsonProperty
    public List<Event> getData() {
        return data;
    }
}
