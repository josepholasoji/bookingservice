package com.musala.services.booking.models.requests;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.musala.services.booking.Enums.EventCategories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateEventRequest {

    @NotBlank(message = "Event name is mandatory")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Reservation date and time is mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank(message = "Event description is mandatory")
    private String description;

    private EventCategories category = EventCategories.Concert;

    private int availableAttendeesCount = 100;

    public CreateEventRequest() {
    }

    public CreateEventRequest(String name, Date date, String description, String category, int availableAttendeesCount) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.category = EventCategories.valueOf(category);
        this.availableAttendeesCount = availableAttendeesCount;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category.toString();
    } 

    public int getAvailableAttendeesCount() {
        return availableAttendeesCount;
    }
}
