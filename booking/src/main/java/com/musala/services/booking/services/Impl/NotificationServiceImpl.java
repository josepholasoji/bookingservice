package com.musala.services.booking.services.Impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.musala.services.booking.models.Notification;
import com.musala.services.booking.models.Ticket;
import com.musala.services.booking.services.EventService;
import com.musala.services.booking.services.NotificationHandler;
import com.musala.services.booking.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${scheduling.fixedRate}")
    private long fixedRate;

    @Value("${eventnotification.limit}")
    private int notificationLimit;

    @Value("${eventnotification.name}")
    private String notificationName;

    private EventService eventService;
    private JmsTemplate jmsTemplate;

    public NotificationServiceImpl(EventService eventService, JmsTemplate jmsTemplate) {
        this.eventService = eventService;
        this.jmsTemplate = jmsTemplate;
    }

    private List<Notification> registry = new ArrayList<>(); 

    @JmsListener(destination = "booking")
    public void receiveMessage(Notification message) {
        System.out.println("Received notification event..." + message);
        for(int i = 0; i < registry.size(); i++) {
            if (registry.get(i).getUserId() == message.getUserId() && registry.get(i).getEventId() == message.getEventId()) {
                registry.get(i).incrementSignalCount();
                if (registry.get(i).getHandler() != null) {
                    registry.get(i).getHandler().handleNotification(message.getUserId(), message.getEventId(), message.getTicket());
                }
            }
        }
    }

    @Scheduled(fixedRateString = "${scheduling.fixedRate}")
    public void performTask() {
        System.out.println("Checking for started events or events that are about to start");
        registry.forEach(notification -> {
            boolean isEventStarted = eventService.isEventStarted(notification.getEventId());
            boolean isEventAboutToStart = eventService.isEventAboutToStart(notification.getEventId());

            // We can only notify the user twice. If the event has started or is about to start, we send a notification
            if (isEventStarted || isEventAboutToStart) {
                int notificationLogged = eventService.logEventNotification(notification.getEventId(), notification.getUserId(), notificationLimit);
                if (notificationLogged < notificationLimit) {
                    try{
                        Notification notificationMessage = new Notification(notification.getUserId(), notification.getEventId(), null);
                        Ticket ticket = eventService.getBooking(notification.getEventId(), notification.getUserId());
                        notificationMessage.setTicket(ticket);
                        jmsTemplate.convertAndSend(notificationName, notificationMessage);
                    } catch (Exception e) {
                        System.out.println("Error sending notification: " + e.getMessage());
                    }   
                }
            }
        });
    }

    @Override
    public void registerEventNotification(int userId, int eventId, NotificationHandler handler) {
        for(int i = 0; i < registry.size(); i++) {
            if (registry.get(i).getUserId() == userId && registry.get(i).getEventId() == eventId) {
                return;
            }
        }

        registry.add(new Notification(userId, eventId, handler));
    }

    @Override
    public Integer getNotificationSignalCount(int userId, int eventId) {
        for(int i = 0; i < registry.size(); i++) {
            if (registry.get(i).getUserId() == userId && registry.get(i).getEventId() == eventId) {
                return registry.get(i).getSignalCount();
            }
        }

        throw new IllegalArgumentException("No such notification registered");
    }
}
