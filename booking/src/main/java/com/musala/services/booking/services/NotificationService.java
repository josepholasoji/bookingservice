package com.musala.services.booking.services;

public interface NotificationService {
    public void registerEventNotification(int userId, int eventId, NotificationHandler handler);
    public Integer getNotificationSignalCount(int userId, int eventId);
}
