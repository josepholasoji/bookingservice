package com.musala.services.booking.models.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.musala.services.booking.configuration.ResponseDeserializer;
import com.musala.services.booking.models.Event;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse extends Response {

    @JsonDeserialize(using = ResponseDeserializer.class)
    private final Event data;

    public EventResponse(int id, String name, Date date, Date enDate, String description, String category, int availableSpaceForAttendees, Date createdOn, Date updatedOn, boolean isActive) {
        this.data = new Event(id, name, date, enDate, description, category, availableSpaceForAttendees, createdOn, updatedOn, isActive);
    }

    public EventResponse(Event event) {
        this.data = event;
    }

    public Event getData() {
        return data;
    }
}
