package com.musala.services.booking.services.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.musala.services.booking.exceptions.EventNotFoundException;
import com.musala.services.booking.exceptions.UserNotFoundException;
import com.musala.services.booking.models.*;
import com.musala.services.booking.repository.EventsDao;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.EventService;

import jakarta.transaction.Transactional;

@Service
public class EventServiceImpl implements EventService {

    private EventsDao eventsDao;
    private UserDao userDao;

    public EventServiceImpl(EventsDao eventsDao, UserDao userDao) {
        this.eventsDao = eventsDao;
        this.userDao = userDao;
    }

    @Transactional
    public Integer createEvent(Event entity) {
        Event event = eventsDao.getEventByName(entity.getName());
        if(event != null) {
           return event.getId();
        }
       return eventsDao.createEvent(entity.getName(), entity.getDescription(), entity.getAvailableAttendeesCount(), entity.getDate(), entity.getEndDate(), entity.getCategory());
    }

    @Transactional
    public List<Event> getAllEvents(String eventSearchPhrase) {
        if (eventSearchPhrase == null || eventSearchPhrase.isBlank()) {
            return eventsDao.getAllEvents();            
        }

        return eventsDao.searchEventByName(eventSearchPhrase);
    }

    @Transactional
    public Event getEventById(int eventId) {
        Event event = eventsDao.getEventById(eventId);
        return event;
    }

    @Transactional
    public Ticket bookTickets(int eventId, int userId) {
        Event event = eventsDao.getEventById(eventId);
        if(event == null) {
            throw new EventNotFoundException("Event with id " + eventId + " not found");
        }

        User user = userDao.getUserById(userId);
        if(user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        Integer id = eventsDao.bookTickets(eventId, userId);
        return new Ticket(id, eventId, userId, new Date());
    }

    @Transactional
    public Event deleteEventById(int eventId) {
        return eventsDao.deleteEventById(eventId);
    }

    @Override
    public Void deleteBooking(int eventId, int ticketId) {
        return eventsDao.deleteBooking(eventId, ticketId);
    }

    @Override
    public Ticket getBooking(int eventId, int userId) {
        return eventsDao.getBooking(eventId, userId);
    }

    @Override
    public boolean isEventStarted(int eventId) {
        return eventsDao.isEventStarted(eventId);
    }

    @Override
    public int logEventNotification(int eventId, int userId, int notificationLimit) {
        return eventsDao.logEventNotification(eventId, userId, notificationLimit);
    }

    @Override
    public boolean isEventAboutToStart(int eventId) {
        return eventsDao.isEventAboutToStart(eventId);
    }
}
