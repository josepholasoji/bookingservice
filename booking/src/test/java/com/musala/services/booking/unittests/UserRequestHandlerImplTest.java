package com.musala.services.booking.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.musala.services.booking.models.User;
import com.musala.services.booking.models.requests.CreateUserRequest;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.models.responses.UsersResponse;
import com.musala.services.booking.requesthandlers.Impl.UserRequestHandlerImpl;
import com.musala.services.booking.services.UserService;


public class UserRequestHandlerImplTest {
    @Mock
    private UserService userService;

    private UserRequestHandlerImpl userRequestHandler;

    private int id = 1;
    private String name = "test";
    private String email = "test@test.com";
    private String password = "password";
    private String role = "user";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequestHandler = new UserRequestHandlerImpl(userService);
    }

    @SuppressWarnings("null")
    @Test
    public void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest(name, email, password, role);
        UserResponse userResponse = new UserResponse(id, name, email, role);
        when(userService.createUser(any(User.class))).thenReturn(id);

        ResponseEntity<UserResponse> result = userRequestHandler.createUser(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());        
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getData());
        assertEquals(userResponse.getData().getName(), result.getBody().getData().getName());
        assertEquals(userResponse.getData().getEmail(), result.getBody().getData().getEmail());
        assertEquals(userResponse.getData().getRole(), result.getBody().getData().getRole());
    }

    @SuppressWarnings("null")
    @Test
    public void testGetUserById() {
        User user = new User(name, email, password, role);
        UserResponse userResponse = new UserResponse(user);
        when(userService.getUserById(any(Integer.class))).thenReturn(user);

        ResponseEntity<UserResponse> result = userRequestHandler.getUserById(id);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getData()); 
        assertEquals(userResponse.getData().getName(), result.getBody().getData().getName());
        assertEquals(userResponse.getData().getEmail(), result.getBody().getData().getEmail());
        assertEquals(userResponse.getData().getRole(), result.getBody().getData().getRole());
    }

    @SuppressWarnings("null")
    @Test
    public void testGetAllUsers() {
        User user1 = new User(name, email, password, role);
        User user2 = new User(name, email, password, role);
        List<User> users = Arrays.asList(user1, user2);
        UsersResponse usersResponse = new UsersResponse(users);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<UsersResponse> result = userRequestHandler.getAllUsers();
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getData());
        assertEquals(usersResponse.getData().size(), result.getBody().getData().size());
        assertEquals(usersResponse.getData().get(0).getName(), result.getBody().getData().get(0).getName());
        assertEquals(usersResponse.getData().get(0).getEmail(), result.getBody().getData().get(0).getEmail());
        assertEquals(usersResponse.getData().get(0).getRole(), result.getBody().getData().get(0).getRole());
        assertEquals(usersResponse.getData().get(1).getName(), result.getBody().getData().get(1).getName());
        assertEquals(usersResponse.getData().get(1).getEmail(), result.getBody().getData().get(1).getEmail());
        assertEquals(usersResponse.getData().get(1).getRole(), result.getBody().getData().get(1).getRole());
    }

    @SuppressWarnings("null")
    @Test
    public void testGetUser() {
        User user = new User(name, email, password, role);
        UserResponse userResponse = new UserResponse(id, name, email, role);
        when(userService.getUserById(any(Integer.class))).thenReturn(user);

        ResponseEntity<UserResponse> result = userRequestHandler.getUser(id);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getData());
        assertEquals(userResponse.getData().getName(), result.getBody().getData().getName());
        assertEquals(userResponse.getData().getEmail(), result.getBody().getData().getEmail());
        assertEquals(userResponse.getData().getRole(), result.getBody().getData().getRole());
    }

    @Test
    public void testDeleteUser() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        ResponseEntity<Void> result = userRequestHandler.deleteUser(id);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(responseEntity, result);
    }
}