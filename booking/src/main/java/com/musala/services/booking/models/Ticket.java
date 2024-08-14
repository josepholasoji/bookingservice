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
public class Ticket implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column(name = "event_id")
    private Integer eventId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "created_at")
    private Date bookedOn;

    public Ticket() {
        this.bookedOn = new Date();
        this.eventId = 0;
        this.userId = 0;
    }

    public Ticket(Integer id, Integer eventId, Integer userId, Date bookedOn) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.bookedOn = bookedOn;
    }

    public Ticket(Integer eventId, Integer userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.bookedOn = new Date();
    }

    public Integer getId() {
        return id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Date getBookedOn() {
        return bookedOn;
    }
}
