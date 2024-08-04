package com.musala.services.booking.models;

import java.io.Serializable;
import com.musala.services.booking.services.NotificationHandler;

public class Notification implements Serializable {
    private int userId;
    private int eventId;
    private int signalCount;
    private Ticket ticket;
    private NotificationHandler handler;

    public Notification() {
    }

    public Notification(int userId, int eventId,  NotificationHandler handler) {
        this.userId = userId;
        this.eventId = eventId;
        this.handler = handler;
        this.signalCount = 0;
        this.ticket = null;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }

    public NotificationHandler getHandler() {
        return handler;
    }

    public int getSignalCount() {
        return signalCount;
    }

    public void incrementSignalCount() {
        signalCount++;
    }
}
