package com.musala.services.booking.models.responses;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.musala.services.booking.configuration.ResponseDeserializer;
import com.musala.services.booking.models.Ticket;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketsResponse extends Response {

    @JsonDeserialize(using = ResponseDeserializer.class)
    private List<Ticket> data;

    public TicketsResponse() {
        super();
    }

    public TicketsResponse(List<Ticket> events) {
        this.data = events;
    }

    @JsonProperty
    public List<Ticket> getData() {
        return data;
    }

}
