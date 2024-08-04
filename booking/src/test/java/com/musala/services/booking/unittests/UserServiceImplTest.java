package com.musala.services.booking.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.exceptions.DuplicateEntryException;
import com.musala.services.booking.models.User;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.Impl.UserServiceImpl;

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String USER_NAME = "Sample User";
    private static final String USER_EMAIL = "sample@admin.com";
    private static final String USER_PASSWORD = "admin";
    private static final String USER_ROLE = UserCategories.Admin.toString();

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserById() {
        int userId = 1;
        User expectedUser = new User(USER_NAME, USER_EMAIL, USER_PASSWORD, USER_ROLE);
        when(userDao.getUserById(userId)).thenReturn(expectedUser);

        User result = userService.getUserById(userId);

        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "john.doe@example.com";
        User expectedUser = new User(USER_NAME, email, USER_PASSWORD, USER_ROLE);
        when(userDao.getUserByEmail(email)).thenReturn(expectedUser);

        User result = userService.getUserByEmail(email);

        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetAllUsers() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User("John Doe", "john.doe@example.com", "password", "ROLE_USER"));
        expectedUsers.add(new User("Jane Smith", "jane.smith@example.com", "password", "ROLE_USER"));
        when(userDao.getAllUsers()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();
        assertEquals(expectedUsers, result);
    }

    @Test
    public void testDeleteUser() {
        int userId = 1;

        userService.deleteUser(userId);

        //
        //
        // Verify that the deleteUser method of userDao is called with the correct
        // userId
        verify(userDao, times(1)).deleteUser(userId);
    }

    @Test
    public void testCreateUser() {
        User user = new User("John Doe", "john.doe@example.com", "password", "ROLE_USER");
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(null);

        when(userDao.createUser(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()),
                user.getRole())).thenReturn(1);
        int result = userService.createUser(user);
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        assertThrows(DuplicateEntryException.class, () -> {
            User user = new User("John Doe", "john.doe@example.com", "password", "ROLE_USER");
            when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
            userService.createUser(user);
        });
    }
}