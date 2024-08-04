package com.musala.services.booking.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.musala.services.booking.Enums.EventCategories;
import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.exceptions.UserNotFoundException;
import com.musala.services.booking.models.User;
import com.musala.services.booking.models.requests.AuthenticationRequest;
import com.musala.services.booking.models.requests.CreateEventRequest;
import com.musala.services.booking.models.requests.CreateUserRequest;
import com.musala.services.booking.models.responses.AuthenticationResponse;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.ExceptionResponse;
import com.musala.services.booking.models.responses.TicketResponse;
import com.musala.services.booking.models.responses.TicketsResponse;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.services.EventService;
import com.musala.services.booking.services.NotificationService;
import com.musala.services.booking.services.UserService;

import ch.qos.logback.core.util.StringUtil;
import org.springframework.jms.core.JmsTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiRestControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    NotificationService notificationService;

    private static String token = null;
    private static String usersUrl = null, eventsUrl = null, authUrl = null;

    private static final String USER_NAME = "Sample User";
    private static final String USER_EMAIL = "sample@admin.com";
    private static final String USER_PASSWORD = "adminadmin";
    private static final String USER_PASSWORD_NEW = "weghh3232wew3";

    static final String EVENT_NAME = "Sample event";
    static final Date EVENT_DATE = new Date();
    static final String EVENT_DESCRIPTION = "a sample event";
    static final String EVENT_CATEGORY = EventCategories.Concert.toString();
    static final int EVENT_CAPACITY = 100;

    @BeforeEach
    @SuppressWarnings("null")
    public void before() throws JsonMappingException, JsonProcessingException {
        String host = "http://localhost:" + port;
        String baseUrl = host + "/booking/api";
        authUrl = baseUrl + "/auth";
        usersUrl = baseUrl + "/users";
        eventsUrl = baseUrl + "/events";

        // We try to get the auth token here even though its not needed for the only
        // exposed user endpoint.
        if (StringUtil.isNullOrEmpty(token)) {
            User user = userService.getUserByEmail(USER_EMAIL);
            if (user == null) {
                userService.createUser(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));
            }

            RequestEntity<AuthenticationRequest> request = RequestEntity.post(authUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthenticationRequest(USER_EMAIL, USER_PASSWORD));

            AuthenticationResponse response = restTemplate.exchange(request, AuthenticationResponse.class).getBody();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals("Success", response.getMessage());
            token = response.getData();
        }
    }

    @Test
    public void testUserApiControllerCreateEndpoint_InvalidRequestWithMissingUserName() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CreateUserRequest(null, USER_EMAIL, USER_PASSWORD_NEW, UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(usersUrl, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"name\":\"Username is mandatory\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestWithMissingPassword() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CreateUserRequest(USER_NAME, USER_EMAIL, "", UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(usersUrl, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"password\":\"Password is mandatory\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestWithMissingEmail() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CreateUserRequest(USER_NAME, "", USER_PASSWORD_NEW, UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(usersUrl, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"email\":\"Email is mandatory\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestWithUserNameLongerThanExpectedLength()
            throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CreateUserRequest(
                        "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmsdsdsdsdsssssssssssssssssssssssssssssssss",
                        USER_EMAIL, USER_PASSWORD_NEW, UserCategories.Admin.toString()));

        ExceptionResponse response = this.restTemplate.postForObject(usersUrl, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"name\":\"Username must be between 3 and 100 characters\"}", response.getData());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequestDuplicateEntry() throws Exception {
        RequestEntity<CreateUserRequest> request = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CreateUserRequest(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));

        this.restTemplate.postForObject(usersUrl, request, ExceptionResponse.class);
        ExceptionResponse response = this.restTemplate.postForObject(usersUrl, request, ExceptionResponse.class);
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
    }

    @Test
    public void testUserApiControllerCreateEndpoint_ValidRequest() throws Exception {
        // Clear all the existing users
        userService.getAllUsers().forEach(user -> userService.deleteUser(user.getId()));

        // Test the create endpoint
        RequestEntity<CreateUserRequest> request = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CreateUserRequest(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));

        UserResponse response = this.restTemplate.postForObject(usersUrl, request, UserResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @SuppressWarnings("null")
@Test
    // This test:
    // 1. creates a new user
    // 2. login the new created user and get the auth token to use for the rest of the tests
    // 3. creates an event
    // 4. search the event and ...
    // 5. books the user for the event
    // 6. wait for the booking to signal that the event is about to start (for this we wait on a notification queue - we use rabbit mq)
    // 7.0. then deletes the booking
    // 7.1. confirms that the booking has been deleted by getting the users bookings and checking that the booking is not there
    // 7.2. deletes the event
    // 7.3. deletes the user
    // 8. confirms that the event has been signaled or not
    public void functionalRequirementTest() throws Exception {
        // Clear all the existing users
        userService.getAllUsers().forEach(user -> userService.deleteUser(user.getId()));

        eventService.getAllEvents("").forEach(event -> eventService.deleteEventById(event.getId()));

        // Create a new user
        RequestEntity<CreateUserRequest> requestCreateUserRequest = RequestEntity.post(usersUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CreateUserRequest(USER_NAME, USER_EMAIL, USER_PASSWORD, UserCategories.Admin.toString()));

        UserResponse createUserRequestResponse = this.restTemplate.postForObject(usersUrl, requestCreateUserRequest,
                UserResponse.class);
        assertEquals(HttpStatus.OK.value(), createUserRequestResponse.getStatus());
        assertEquals("Success", createUserRequestResponse.getMessage());

        // Login the user and get the auth token
        User user = userService.getUserByEmail(USER_EMAIL);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        RequestEntity<AuthenticationRequest> request = RequestEntity.post(authUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AuthenticationRequest(user.getEmail(), USER_PASSWORD));

        AuthenticationResponse authenticationResponse = restTemplate.exchange(request, AuthenticationResponse.class).getBody();
        assertEquals(HttpStatus.OK.value(), authenticationResponse.getStatus());
        assertEquals("Success", authenticationResponse.getMessage());
        token = authenticationResponse.getData();

        // Create an event...
        RequestEntity<CreateEventRequest> requestCreateEventRequest = RequestEntity.post(eventsUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CreateEventRequest(EVENT_NAME, EVENT_DATE, EVENT_DESCRIPTION, EVENT_CATEGORY,
                        EVENT_CAPACITY));

        EventResponse createEventRequestResponse = this.restTemplate.postForObject(eventsUrl, requestCreateEventRequest,
                EventResponse.class);
        assertEquals(HttpStatus.OK.value(), createEventRequestResponse.getStatus());
        assertEquals("Success", createEventRequestResponse.getMessage());

        // Search for the event
        String searchForEvent = eventsUrl + "/names/" + EVENT_NAME;
        HttpHeaders searchForEventHeaders = new HttpHeaders();
        searchForEventHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(searchForEventHeaders);

        EventsResponse searchForEventResponse = this.restTemplate
                .exchange(searchForEvent, HttpMethod.GET, entity, EventsResponse.class).getBody();
        assertEquals(HttpStatus.OK.value(), searchForEventResponse.getStatus());
        assertEquals("Success", searchForEventResponse.getMessage());
        assertEquals(1, searchForEventResponse.getData().size());

        // Book the user for the event
        String bookTicketUrl = eventsUrl + "/" + createEventRequestResponse.getData().getId() + "/tickets";
        HttpHeaders headersTicketsResponse = new HttpHeaders();
        headersTicketsResponse.set("Authorization", "Bearer " + token);
        HttpEntity<String> entityTicketsResponse = new HttpEntity<>(headersTicketsResponse);

        TicketResponse ticketResponseResponse = this.restTemplate
                .exchange(bookTicketUrl, HttpMethod.GET, entityTicketsResponse, TicketResponse.class).getBody();
        assertEquals(HttpStatus.OK.value(), ticketResponseResponse.getStatus());
        assertEquals("Success", ticketResponseResponse.getMessage());

        // register and wait for the event to start...
        notificationService.registerEventNotification(createUserRequestResponse.getData().getId(),
                createEventRequestResponse.getData().getId(), (userId, eventId, ticket) -> {
                    // Delete the booking
                    String deleteBookingUrl = eventsUrl + "/" + eventId + "/tickets/" + ticket.getId();
                    HttpHeaders deleteBookingUrlHeader = new HttpHeaders();
                    deleteBookingUrlHeader.set("Authorization", "Bearer " + token);
                    HttpEntity<String> entityDelete = new HttpEntity<>(deleteBookingUrlHeader);

                    this.restTemplate.exchange(deleteBookingUrl, HttpMethod.DELETE, entityDelete, String.class).getBody();

                    // Confirm that the booking has been deleted by getting the users bookings and
                    // checking that the booking is not there
                    String userBookingsUrl = usersUrl + "/events";
                    HttpHeaders userBookingsUrlHeader = new HttpHeaders();
                    userBookingsUrlHeader.set("Authorization", "Bearer " + token);
                    HttpEntity<String> entityBookingEntries = new HttpEntity<>(userBookingsUrlHeader);

                    TicketsResponse userBookingsResponse = this.restTemplate.exchange(userBookingsUrl, HttpMethod.GET, entityBookingEntries, TicketsResponse.class).getBody();
                    assertEquals(HttpStatus.OK.value(), userBookingsResponse.getStatus());
                    assertEquals("Success", userBookingsResponse.getMessage());
                    assertEquals(0, userBookingsResponse.getData().size());

                    // Delete the user
                    userService.deleteUser(userId);

                    // Delete the events
                    eventService.deleteEventById(eventId);
                });

        // Wait for 5 seconds for the event to start
        int userId = createUserRequestResponse.getData().getId();
        int eventId = createEventRequestResponse.getData().getId();
        int timeOut = 40000; // 10 seconds
        int timeElapsed = 0;
        while (timeElapsed < timeOut) {
            Thread.sleep(1000);
            timeElapsed += 1000;
        }

        // Check if the event has been signaled or not
        assertEquals(1, notificationService.getNotificationSignalCount(userId, eventId), "Event timed out");
    }
}
