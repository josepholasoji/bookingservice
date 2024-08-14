package com.musala.services.booking.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.musala.services.booking.models.Event;

@Repository
public interface EventsDao extends JpaRepository<Event, Integer> {

    @Procedure(value = "add_event")
    public Integer createEvent(@Param("p_name") String name, @Param("p_description") String description,
            @Param("p_capacity") int capacity, @Param("p_start_date") Date startDate, @Param("p_end_date") Date endDate,
            @Param("p_category") String category);

    @Procedure(value = "get_all_events")
    public List<Event> getAllEvents();

    @Procedure(value = "get_event_by_name")
    public Event getEventByName(@Param("p_name") String name);

    @Procedure(value = "get_event_by_id")
    public Event getEventById(@Param("p_event_id") int eventId);

    @Procedure(value = "search_event_by_partial_name")
    public List<Event> searchEventByName(@Param("p_name") String name);

    @Procedure(value = "delete_event_by_id")
    public void deleteEventById(@Param("p_id") int eventId);

    @Procedure(value = "book_event")
    public Integer bookTickets(@Param("p_event_id") int eventId, @Param("p_user_id") int userId);

    @Procedure(value = "delete_booking_by_event_id_and_ticket_id")
    public Void deleteBooking(@Param("p_event_id") int eventId, @Param("p_ticket_id") int ticketId);

    @Procedure(value = "get_booking_by_event_id_and_user_id")
    public Integer getBooking(@Param("p_event_id") int eventId, @Param("p_user_id") int userId);

    @Procedure(value = "is_event_started")
    public boolean isEventStarted(@Param("p_event_id") int eventId);

    @Procedure(value = "log_event_notification")
    public int logEventNotification(@Param("p_event_id") int eventId, @Param("p_user_id") int userId, @Param("p_limit") int notificationLimit);

    @Procedure(value = "is_event_about_to_start")
    public boolean isEventAboutToStart(@Param("p_event_id") int eventId);
}
