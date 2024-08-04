package com.musala.services.booking.models;

import java.util.Date;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.musala.services.booking.Enums.EventCategories;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private Date date;
    @Column
    private Date end_date;
    @Column
    private String description;
    @Column
    private EventCategories category = EventCategories.Concert;
    @Column(name = "capacity")
    private int availableAttendeesCount;

    @Column(name = "created_at")
    private Date createdOn;
    @Column(name = "updated_at")
    private Date updatedOn;
    @Column(name = "is_active")
    private boolean isActive;

    public Event() {
        this.createdOn = new Date();
        this.updatedOn = new Date();
        this.isActive = true;
    }

    public Event(String name, Date date, Date enDate, String description, String category, int availableAttendeesCount) {
        this.name = name;
        this.date = date;
        this.end_date = enDate;
        this.description = description;
        this.category = EventCategories.valueOf(category);
        this.availableAttendeesCount = availableAttendeesCount;
        this.createdOn = new Date();
        this.updatedOn = new Date();
        this.isActive = true;
    }

    public Event(int id, String name, Date date, Date enDate, String description, String category, int availableAttendeesCount, Date createdOn, Date updatedOn, boolean isActive) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.end_date = enDate;
        this.description = description;
        this.category = EventCategories.valueOf(category);
        this.availableAttendeesCount = availableAttendeesCount;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        if (date == null) {
            return new Date();            
        }
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category.toString();
    }

    public int getId() {
        return id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getAvailableAttendeesCount() {
        return availableAttendeesCount;
    }

    public Date getEndDate() {
        if(end_date == null) {
            // if end date is not set, return the same as start date + 1hr
            return new Date(date.getTime() + 3600000);
        }
        return end_date;
    }
}
