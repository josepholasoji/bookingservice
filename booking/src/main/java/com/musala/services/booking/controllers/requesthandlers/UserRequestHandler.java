package com.musala.services.booking.controllers.requesthandlers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.requests.CreateUserRequest;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.models.responses.UsersResponse;

public interface UserRequestHandler {

    public ResponseEntity<UserResponse> createUser(CreateUserRequest user);

    public ResponseEntity<UserResponse> getUserById(int id);

    public ResponseEntity<UsersResponse> getAllUsers();

    public ResponseEntity<UserResponse> getUser(int id);

    public ResponseEntity<Void> deleteUser(int id);

    public List<Event> getUserRegisteredEventsById(String username);
}
