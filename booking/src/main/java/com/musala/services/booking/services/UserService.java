package com.musala.services.booking.services;

import java.util.List;

import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.User;

public interface UserService {

    public User getUserById(int id);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public void deleteUser(int id);

    public int createUser(User user);

    public List<Event> getUserRegisteredEventsById(int id);
}
