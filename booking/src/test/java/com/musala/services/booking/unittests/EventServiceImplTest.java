package com.musala.services.booking.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.musala.services.booking.Enums.EventCategories;
import com.musala.services.booking.exceptions.EventNotFoundException;
import com.musala.services.booking.exceptions.UserNotFoundException;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.Ticket;
import com.musala.services.booking.models.User;
import com.musala.services.booking.repository.EventsDao;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.Impl.EventServiceImpl;

public class EventServiceImplTest {

    private EventServiceImpl eventService;
    private EventsDao eventsDao;
    private UserDao userDao;

    static final String EVENT_NAME = "Sample event";
    static final Date EVENT_DATE = new Date();
    static final String EVENT_DESCRIPTION = "a sample event";
    static final String EVENT_CATEGORY = EventCategories.Concert.toString();
    static final int EVENT_CAPACITY = 100;    

    @BeforeEach
    public void setUp() {
        eventsDao = mock(EventsDao.class);
        userDao = mock(UserDao.class);
        eventService = new EventServiceImpl(eventsDao, userDao);
    }

    @Test
    public void testCreateEvent() {
        //(String name, Date date, Date enDate, String description, String category, int availableAttendeesCount) 
        Event event = new Event(EVENT_NAME, EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION,  EVENT_CATEGORY, EVENT_CAPACITY);
        when(eventsDao.getEventByName(event.getName())).thenReturn(null);
        when(eventsDao.createEvent(any(String.class), any(String.class), any(Integer.class), any(Date.class), any(Date.class), any(String.class))).thenReturn(1);

        Integer eventId = eventService.createEvent(event);
        assertEquals(1, eventId);
    }

    @Test
    public void testCreateEvent_ExistingEvent() {
        Event event = new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY);
        when(eventsDao.getEventByName(event.getName())).thenReturn(event);

        Integer eventId = eventService.createEvent(event);
        assertEquals(event.getId(), eventId);
    }

    @Test
    public void testGetAllEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY));
        events.add(new Event(EVENT_NAME + "2", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "2",  EVENT_CATEGORY, EVENT_CAPACITY));
        when(eventsDao.getAllEvents()).thenReturn(events);

        List<Event> result = eventService.getAllEvents(null);
        assertEquals(events, result);
    }

    @Test
    public void testGetAllEvents_WithSearchPhrase() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY));
        when(eventsDao.searchEventByName("Event")).thenReturn(events);

        List<Event> result = eventService.getAllEvents("Event");
        assertEquals(events, result);
    }

    @Test
    public void testGetEventById() {
        Event event = new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY);
        when(eventsDao.getEventById(1)).thenReturn(event);

        Event result = eventService.getEventById(1);
        assertEquals(event, result);
    }

    @Test
    public void testBookTickets() {
        Event event = new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY);
        User user = new User("Test User", "test@example.com", "password", "user");
        when(eventsDao.getEventById(1)).thenReturn(event);
        when(userDao.getUserById(1)).thenReturn(user);
        when(eventsDao.bookTickets(1, 1)).thenReturn(1);

        Ticket ticket = eventService.bookTickets(1, 1);
        assertEquals(1, ticket.getId());
        assertEquals(1, ticket.getEventId());
        assertEquals(1, ticket.getUserId());
    }

    @Test
    public void testBookTickets_EventNotFound() {
        User user = new User("Test User", "test@example.com", "password", "user");
        when(eventsDao.getEventById(1)).thenReturn(null);
        when(userDao.getUserById(1)).thenReturn(user);

        assertThrows(EventNotFoundException.class, () -> {
            eventService.bookTickets(1, 1);
        });
    }

    @Test
    public void testBookTickets_UserNotFound() {
        Event event = new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY);
        when(eventsDao.getEventById(1)).thenReturn(event);
        when(userDao.getUserById(1)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            eventService.bookTickets(1, 1);
        });
    }

    @Test
    public void testDeleteEventById() {
        Event event = new Event(EVENT_NAME + "1", EVENT_DATE, EVENT_DATE, EVENT_DESCRIPTION + "1",  EVENT_CATEGORY, EVENT_CAPACITY);
        when(eventsDao.deleteEventById(1)).thenReturn(event);

        Event result = eventService.deleteEventById(1);
        assertEquals(event, result);
    }
}