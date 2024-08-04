package com.musala.services.booking.services;

import java.util.List;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.Ticket;

public interface EventService {
    public Integer createEvent(Event entity);
    public List<Event> getAllEvents(String eventSearchPhrase);
    public Event getEventById(int eventId);
    public Event deleteEventById(int eventId);
    public Ticket getBooking(int eventId, int userId);
    public Ticket bookTickets(int eventId, int userId);
    public Void deleteBooking(int eventId, int ticketId);
    public boolean isEventStarted(int eventId);
    public int logEventNotification(int eventId, int userId, int notificationLimit);
    public boolean isEventAboutToStart(int eventId);
} 