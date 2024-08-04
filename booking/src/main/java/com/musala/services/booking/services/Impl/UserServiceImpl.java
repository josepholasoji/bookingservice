package com.musala.services.booking.services.Impl;

import java.util.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.exceptions.DuplicateEntryException;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.User;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.UserService;
import org.springframework.core.io.Resource;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    private static final String ADMIN_EMAIL = "anadmi@email.com";
    private static final String ADMIN_PASSWORD = "anadminpassword";
    private static final String ADMIN_NAME = "Admin TestAccount";
    private static final String ADMIN_ROLE = UserCategories.Admin.toString();

    // I'm adding this method to ensure there is a always a user with admin role
    // We need to run this like this to ensure that the admin password is hashed in the db
    @PostConstruct
    public void onStartup() {

        // Load the databases 
        Resource resource = new ClassPathResource("migration/databases.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.execute(dataSource);

        // Load the stored procedures - events
        resource = new ClassPathResource("migration/procedures/events.sql");
        databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.execute(dataSource);

        // Load the stored procedures - users
        resource = new ClassPathResource("migration/procedures/users.sql");
        databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.execute(dataSource);

       if(this.getUserByEmail(ADMIN_EMAIL) == null) {
            new User(ADMIN_NAME, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_ROLE);
       }
    }

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
    @Transactional
    public List<Event> getUserRegisteredEventsById(int id) {
        return userDao.getUserRegisteredEventsById(id);
    }
}
