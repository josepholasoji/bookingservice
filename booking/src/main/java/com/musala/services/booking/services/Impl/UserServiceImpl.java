package com.musala.services.booking.services.Impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.musala.services.booking.exceptions.DuplicateEntryException;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.Ticket;
import com.musala.services.booking.models.User;
import com.musala.services.booking.repository.EventsDao;
import com.musala.services.booking.repository.TicketDao;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventsDao eventsDao;

    @Value("${sample.user.email}")
    private String ADMIN_EMAIL;
    @Value("${sample.user.password}")
    private String ADMIN_PASSWORD;
    @Value("${sample.user.name}")
    private String ADMIN_NAME;
    @Value("${sample.user.role}")
    private String ADMIN_ROLE;

    // I'm adding this method to ensure there is always a user with admin role
    // We need to run this like this to ensure that the admin password is hashed in the db
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;
        
    @PostConstruct
    public void onStartup() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @SuppressWarnings("null")
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if(status != null && status.isCompleted()){
                    User user = getUserByEmail(ADMIN_EMAIL);
                    if(user != null) {
                        deleteUser(user.getId());
                    }

                    createUser(new User(ADMIN_NAME, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_ROLE));
                }
            }
        });
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
        List<Ticket> bookings = ticketDao.getUserRegisteredEventsById(id);
        List<Event> events = new ArrayList<>();
        for (Ticket ticket : bookings) {
            events.add(eventsDao.getEventById(ticket.getEventId()));
        }
        return events;
    }
}
