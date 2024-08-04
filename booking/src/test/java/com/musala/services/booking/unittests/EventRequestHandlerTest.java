package com.musala.services.booking.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.Locale.Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.musala.services.booking.Enums.EventCategories;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.requests.CreateEventRequest;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.TicketResponse;
import com.musala.services.booking.requesthandlers.EventRequestHandler;

@SuppressWarnings("null")
public class EventRequestHandlerTest {
    @Mock
    private EventRequestHandler eventRequestHandler;

    private String name = "test";
    private Date date = new Date();
    private Date enDate = new Date();
    private String description = "test";
    private String category = EventCategories.Concert.toString();
    private int availableSpaceForAttendees = 1;
    private Date createdOn = new Date();
    private Date updatedOn = new Date();
    private boolean isActive = true;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEvent() {
        CreateEventRequest request = new CreateEventRequest();
        EventResponse eventResponse = new EventResponse(1, name, date, enDate, description, category, availableSpaceForAttendees, createdOn, updatedOn, isActive);
        ResponseEntity<EventResponse> responseEntity = new ResponseEntity<>(eventResponse, HttpStatus.OK);

        when(eventRequestHandler.createEvent(any(CreateEventRequest.class))).thenReturn(responseEntity);

        ResponseEntity<EventResponse> result = eventRequestHandler.createEvent(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(eventResponse, result.getBody());
    }

    @Test
    public void testGetAllEvents() {
        Event eventResponse1 = new Event(1, name, date, enDate, description, category, availableSpaceForAttendees, createdOn, updatedOn, isActive);
        Event eventResponse2 = new Event(2, name, date, enDate, description, category, availableSpaceForAttendees, createdOn, updatedOn, isActive);
        EventsResponse eventList = new EventsResponse(Arrays.asList(eventResponse1, eventResponse2));
        ResponseEntity<EventsResponse> responseEntity = new ResponseEntity<>(eventList, HttpStatus.OK);

        when(eventRequestHandler.getAllEvents(any(String.class))).thenReturn(responseEntity);

        ResponseEntity<EventsResponse> result = eventRequestHandler.getAllEvents("test");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(eventList, result.getBody());
    }

    @Test
    public void testGetEventById() {
        EventResponse eventResponse = new EventResponse(1, name, date, enDate, description, category, availableSpaceForAttendees, createdOn, updatedOn, isActive);
        ResponseEntity<EventResponse> responseEntity = new ResponseEntity<>(eventResponse, HttpStatus.OK);

        when(eventRequestHandler.getEventById(any(Integer.class))).thenReturn(responseEntity);

        ResponseEntity<EventResponse> result = eventRequestHandler.getEventById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(eventResponse, result.getBody());
    }

    @Test
    public void testBookTickets() {
        TicketResponse ticketResponse = null;// new TicketResponse();
        ResponseEntity<TicketResponse> responseEntity = new ResponseEntity<>(ticketResponse, HttpStatus.OK);

        when(eventRequestHandler.bookTickets(any(Integer.class), any(Integer.class))).thenReturn(responseEntity);

        ResponseEntity<TicketResponse> result = eventRequestHandler.bookTickets(1, 1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ticketResponse, result.getBody());
    }
}
