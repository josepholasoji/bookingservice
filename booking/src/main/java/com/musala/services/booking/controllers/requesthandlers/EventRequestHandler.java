package com.musala.services.booking.controllers.requesthandlers;

import org.springframework.http.ResponseEntity;

import com.musala.services.booking.models.requests.CreateEventRequest;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.TicketResponse;

public interface EventRequestHandler {
    public ResponseEntity<EventResponse> createEvent(CreateEventRequest entity);
    public ResponseEntity<EventsResponse> getAllEvents(String eventSearchPhrase);
    public ResponseEntity<EventResponse> getEventById(int eventId);
    public ResponseEntity<TicketResponse> bookTickets(int eventId, int userId);
    public ResponseEntity<Void> deleteBooking(int eventId, int ticketId);
} 