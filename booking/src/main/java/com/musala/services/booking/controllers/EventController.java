package com.musala.services.booking.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.services.booking.models.requests.CreateEventRequest;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.TicketResponse;
import com.musala.services.booking.requesthandlers.EventRequestHandler;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRequestHandler eventRequestHandler;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return eventRequestHandler.createEvent(request);
    }

    @GetMapping("/names/{eventSearchPhrase}")
    public ResponseEntity<EventsResponse> getAllEvents(@PathVariable("eventSearchPhrase") String eventSearchPhrase) {
        return eventRequestHandler.getAllEvents(eventSearchPhrase);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("eventId") Integer eventId) {
        return eventRequestHandler.getEventById(eventId);
    }

    @GetMapping("/{eventId}/tickets")
    public ResponseEntity<TicketResponse> bookTickets(@PathVariable("eventId") Integer eventId) {
        // Get the userId from the session
        int userId = 1;
        return eventRequestHandler.bookTickets(eventId, userId);
    }

    @GetMapping("/{eventId}/tickets/{ticketId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("eventId") Integer eventId, @PathVariable("ticketId") Integer ticketId) {
        return eventRequestHandler.deleteBooking(eventId, ticketId);
    }
}
