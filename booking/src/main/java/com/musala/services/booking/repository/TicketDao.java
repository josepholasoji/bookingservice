package com.musala.services.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.musala.services.booking.models.Ticket;

@Repository
public interface TicketDao extends JpaRepository<Ticket, Integer> {
    @Procedure(value =  "get_bookings_by_user_id")
    public List<Ticket> getUserRegisteredEventsById(@Param("p_user_id") int id);
}
