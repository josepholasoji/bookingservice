package com.musala.services.booking.requesthandlers.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.Ticket;
import com.musala.services.booking.models.requests.CreateEventRequest;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.TicketResponse;
import com.musala.services.booking.requesthandlers.EventRequestHandler;
import com.musala.services.booking.services.EventService;

@Component
public class EventRequestHandlerImpl implements EventRequestHandler {

    @Autowired
    private EventService eventService;
    
    @Override
    public ResponseEntity<EventResponse> createEvent(CreateEventRequest entity) {
       Event event = new Event(entity.getName(), entity.getDate(), null, entity.getDescription(), entity.getCategory(), entity.getAvailableAttendeesCount());
       Integer id = eventService.createEvent(event);
       EventResponse eventResponse = new EventResponse(id, event.getName(), event.getDate(), event.getEndDate(), event.getDescription(), event.getCategory(), event.getAvailableAttendeesCount(), event.getCreatedOn(), event.getUpdatedOn(), event.isActive());
       return ResponseEntity.ok(eventResponse);
    }

    @Override
    public ResponseEntity<EventsResponse> getAllEvents(String eventSearchPhrase) {
        List<Event> events = eventService.getAllEvents(eventSearchPhrase);
        EventsResponse eventsResponse = new EventsResponse(events);
        return ResponseEntity.ok(eventsResponse);
    }

    @Override
    public ResponseEntity<EventResponse> getEventById(int eventId) {
        Event event = eventService.getEventById(eventId);
        EventResponse eventResponse = new EventResponse(event.getId(), event.getName(), event.getDate(), event.getEndDate(), event.getDescription(), event.getCategory(), event.getAvailableAttendeesCount(), event.getCreatedOn(), event.getUpdatedOn(), event.isActive());
        return ResponseEntity.ok(eventResponse);
    }

    @Override
    public ResponseEntity<TicketResponse> bookTickets(int eventId, int userId) {
        Ticket ticket = eventService.bookTickets(eventId, userId);
        TicketResponse ticketResponse = new TicketResponse(ticket.getId(), ticket.getEventId(), ticket.getUserId(), ticket.getBookedOn());
        return ResponseEntity.ok(ticketResponse);
    }

    @Override
    public ResponseEntity<Void> deleteBooking(int eventId, int ticketId) {
        eventService.deleteBooking(eventId, ticketId);
        return ResponseEntity.ok().build();
    }    
}
