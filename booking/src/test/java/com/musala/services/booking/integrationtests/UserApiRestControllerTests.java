package com.musala.services.booking.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.models.User;
import com.musala.services.booking.models.requests.AuthenticationRequest;
import com.musala.services.booking.models.requests.CreateUserRequest;
import com.musala.services.booking.models.responses.AuthenticationResponse;
import com.musala.services.booking.models.responses.ExceptionResponse;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.services.UserService;

import ch.qos.logback.core.util.StringUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiRestControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private static String token = null;
    private static String url = null;

    private static final String USER_NAME = "Sample User";
    private static final String USER_EMAIL = "sample@admin.com";
    private static final String USER_PASSWORD = "admin";
    private static final String USER_PASSWORD_NEW = "weghh3232wew3";

    @BeforeEach
    public void before() throws JsonMappingException, JsonProcessingException
    {
        String host = "http://localhost:" + port;
        String baseUrl = host + "/booking/api";
        String authUrl = baseUrl + "/auth";
        url = baseUrl + "/users";
        
        // We try to get the auth token here even though its not needed for the only exposed user endpoint.
        if(StringUtil.isNullOrEmpty(token)){
            User user = userService.getUserByEmail(USER_EMAIL);
            if (user == null) {
                userService.createUser(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));
            }

            RequestEntity<AuthenticationRequest> request = RequestEntity.post(authUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new AuthenticationRequest( USER_EMAIL, USER_PASSWORD));

            AuthenticationResponse response = restTemplate.exchange(request, AuthenticationResponse.class).getBody();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals("Success", response.getMessage());
            token = response.getData();
        }
    }

    @Test
    public void testUserApiControllerCreateEndpoint_InvalidRequestWithMissingUserName() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateUserRequest(null, USER_EMAIL, USER_PASSWORD_NEW, UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"name\":\"Username is mandatory\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestWithMissingPassword() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateUserRequest(USER_NAME, USER_EMAIL, "", UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"password\":\"Password is mandatory\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestWithMissingEmail() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateUserRequest(USER_NAME, "", USER_PASSWORD_NEW, UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"email\":\"Email is mandatory\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestWithUserNameLongerThanExpectedLength() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateUserRequest("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmsdsdsdsdsssssssssssssssssssssssssssssssss", USER_EMAIL, USER_PASSWORD_NEW, UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"name\":\"Username must be between 3 and 100 characters\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestDuplicateEntry() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateUserRequest(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));

        this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequest() throws Exception {
        //Clear all the existing users
        userService.getAllUsers().forEach(user -> userService.deleteUser(user.getId()));

        // Test the create endpoint
        RequestEntity<CreateUserRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new CreateUserRequest(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));

        UserResponse response = this.restTemplate.postForObject(url, request, UserResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Success", response.getMessage());
    }
}
