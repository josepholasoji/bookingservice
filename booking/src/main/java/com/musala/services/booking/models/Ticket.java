package com.musala.services.booking.models;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "bookings")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "event_id")
    private int eventId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "created_at")
    private Date bookedOn;

    public Ticket(int id, int eventId, int userId, Date bookedOn) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.bookedOn = bookedOn;
    }

    public Ticket(int eventId, int userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public int getUserId() {
        return userId;
    }

    public Date getBookedOn() {
        return bookedOn;
    }
}
