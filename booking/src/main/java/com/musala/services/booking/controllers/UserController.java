package com.musala.services.booking.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.services.booking.exceptions.NoUserEventEntryFoundException;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.requests.CreateUserRequest;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.requesthandlers.UserRequestHandler;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRequestHandler userRequestHandler;

    public UserController(UserRequestHandler userRequestHandler) {
        this.userRequestHandler = userRequestHandler;
    }
   
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
       return userRequestHandler.createUser(createUserRequest);
    }

    @GetMapping("/evets")
    public List<Event> getUserEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRequestHandler.getUserRegisteredEventsById(userDetails.getUsername());
        }

        throw new NoUserEventEntryFoundException("No event found for user ");
    }    
}
