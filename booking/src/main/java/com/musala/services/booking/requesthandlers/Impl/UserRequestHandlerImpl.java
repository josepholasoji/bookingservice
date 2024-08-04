package com.musala.services.booking.requesthandlers.Impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.musala.services.booking.requesthandlers.UserRequestHandler;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.User;
import com.musala.services.booking.models.requests.CreateUserRequest;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.models.responses.UsersResponse;
import com.musala.services.booking.services.UserService;

@Component
public class UserRequestHandlerImpl implements UserRequestHandler {

    private UserService userService;

    public UserRequestHandlerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest user) {
        User userEntity = new User(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        int id = userService.createUser(userEntity);
        UserResponse userResponse = new UserResponse(id, userEntity.getName(), userEntity.getEmail(), userEntity.getRole());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(int id) {
        User user = userService.getUserById(id);
        UserResponse userResponse = new UserResponse(id, user.getName(), user.getEmail(), user.getRole());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UsersResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        UsersResponse usersResponse = new UsersResponse(users);
        return new ResponseEntity<>(usersResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserResponse> getUser(int id) {
        User user = userService.getUserById(id);
        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Event> getUserRegisteredEventsById(String username) {
        User user = userService.getUserByEmail(username);
        return userService.getUserRegisteredEventsById(user.getId());
    }
}
