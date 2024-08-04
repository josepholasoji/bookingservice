package com.musala.services.booking.services.Impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.musala.services.booking.exceptions.DuplicateEntryException;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.User;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    @Transactional
    public int createUser(User user) {
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new DuplicateEntryException("User with email " + user.getEmail() + " already exists");
        }

        return userDao.createUser(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()), user.getRole());
    }

    @Override
    public List<Event> getUserRegisteredEventsById(int id) {
        return userDao.getUserRegisteredEventsById(id);
    }
}
