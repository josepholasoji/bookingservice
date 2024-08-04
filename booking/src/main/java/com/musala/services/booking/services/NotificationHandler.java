package com.musala.services.booking.services;

import com.musala.services.booking.models.Ticket;

public interface NotificationHandler {
    public void handleNotification(int userId, int eventId, Ticket event);
}
