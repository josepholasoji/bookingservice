package com.musala.services.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    @Procedure(value =  "get_user_by_id")
    public User getUserById(@Param("p_id") int id);

    @Procedure(value =  "get_users")
    public List<User> getAllUsers();

    @Procedure(value =  "delete_user_by_id")
    public void deleteUser(@Param("p_id") int id);

    @Procedure(value =  "add_user")
    public Integer createUser(@Param("p_name") String name, @Param("p_email")String email, @Param("p_password") String password, @Param("p_role") String role);

    @Procedure(value =  "get_user_by_email")
    public User getUserByEmail(@Param("p_email") String email);

    @Procedure(value =  "get_user_by_email_and_password")
    public User getUserByEmailAndPassword(@Param("p_email") String email, @Param("p_password") String password);

    @Procedure(value =  "get_user_events_by_id")
    public List<Event> getUserRegisteredEventsById(@Param("p_id") int id);
}
