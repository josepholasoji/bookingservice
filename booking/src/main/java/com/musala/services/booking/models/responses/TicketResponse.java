package com.musala.services.booking.models.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.musala.services.booking.configuration.ResponseDeserializer;
import com.musala.services.booking.models.Ticket;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponse extends Response {

    @JsonDeserialize(using = ResponseDeserializer.class)
    private final Ticket data;

    public TicketResponse(int id, int eventId, int userId, Date dateCreated) {
        this.data = new Ticket(id, eventId, userId, dateCreated);
    }

    public Ticket getData() {
        return data;
    }
}
