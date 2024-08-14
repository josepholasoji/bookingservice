package com.musala.services.booking.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.musala.services.booking.Enums.EventCategories;
import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.User;
import com.musala.services.booking.models.requests.AuthenticationRequest;
import com.musala.services.booking.models.requests.CreateEventRequest;
import com.musala.services.booking.models.responses.AuthenticationResponse;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.ExceptionResponse;
import com.musala.services.booking.models.responses.TicketsResponse;
import com.musala.services.booking.services.EventService;
import com.musala.services.booking.services.UserService;

import ch.qos.logback.core.util.StringUtil;

@SuppressWarnings("null")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventApiRestControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    EventService  eventService;

    private static String token = null;
    private static String url = null;

    @Value("${sample.user.email}")
    private String ADMIN_EMAIL;
    @Value("${sample.user.password}")
    private String ADMIN_PASSWORD;
    @Value("${sample.user.name}")
    private String ADMIN_NAME;
    @Value("${sample.user.role}")
    private String ADMIN_ROLE;

    static final String EVENT_NAME = "Sample event";
    static final Date EVENT_DATE = new Date();
    static final String EVENT_DESCRIPTION = "a sample event";
    static final String EVENT_CATEGORY = EventCategories.Concert.toString();
    static final int EVENT_CAPACITY = 100;

    @BeforeEach
    public void before() throws JsonMappingException, JsonProcessingException
    {
        String host = "http://localhost:" + port;
        String baseUrl = host + "/booking/api";
        String authUrl = baseUrl + "/auth";
        url = baseUrl + "/events";
        
        if(StringUtil.isNullOrEmpty(token)){
            User user = userService.getUserByEmail(ADMIN_EMAIL);
            if (user == null) {
                userService.createUser(new User(ADMIN_NAME, ADMIN_EMAIL, ADMIN_PASSWORD, UserCategories.Admin.toString()));
            }

            RequestEntity<AuthenticationRequest> request = RequestEntity.post(authUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new AuthenticationRequest( ADMIN_EMAIL, ADMIN_PASSWORD));

            AuthenticationResponse response = restTemplate.exchange(request, AuthenticationResponse.class).getBody();
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals("Success", response.getMessage());
            token = response.getData();
        }
    }

    @Test
    public void testEventApiControllerCreateEndpoint_InvalidRequestWithMissingEventName() throws Exception {
        RequestEntity<CreateEventRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateEventRequest(null, EVENT_DATE, EVENT_DESCRIPTION,EVENT_CATEGORY, EVENT_CAPACITY));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"name\":\"Event name is mandatory\"}", response.getData());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_ValidRequestWithMissingDescription() throws Exception {
        RequestEntity<CreateEventRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateEventRequest(EVENT_NAME, EVENT_DATE, null,EVENT_CATEGORY, EVENT_CAPACITY));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"description\":\"Event description is mandatory\"}", response.getData());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_ValidRequestWithMissingEventStartDate() throws Exception {
        RequestEntity<CreateEventRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateEventRequest(EVENT_NAME, null, EVENT_DESCRIPTION,EVENT_CATEGORY, EVENT_CAPACITY));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"date\":\"Reservation date and time is mandatory\"}", response.getData());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_ValidRequestWithEventNameLongerThanExpectedLength() throws Exception {
        RequestEntity<CreateEventRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateEventRequest(EVENT_NAME + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", EVENT_DATE, EVENT_DESCRIPTION,EVENT_CATEGORY, EVENT_CAPACITY));

        ExceptionResponse response = this.restTemplate.postForObject(url, request, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
        assertEquals("{\"name\":\"Event name must be between 3 and 100 characters\"}", response.getData());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_ValidRequest() throws Exception {
        RequestEntity<CreateEventRequest> request = RequestEntity.post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(new CreateEventRequest(EVENT_NAME, EVENT_DATE, EVENT_DESCRIPTION,EVENT_CATEGORY, EVENT_CAPACITY));

        EventResponse response = this.restTemplate.postForObject(url, request, EventResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_SearchEvent() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        EventsResponse response = this.restTemplate.exchange(url + "/names/sample", HttpMethod.GET, entity, EventsResponse.class).getBody();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_getEventById() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EventResponse response = this.restTemplate.exchange(url + "/1", HttpMethod.GET, entity, EventResponse.class).getBody();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_bookAnEventWithMissingEvent() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ExceptionResponse  response = this.restTemplate.exchange(url + "/99999/tickets", HttpMethod.GET, entity, ExceptionResponse.class).getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Failed", response.getMessage());
    }

    @Test
    public void testEventApiControllerCreateEndpoint_bookAnEvent() throws Exception {

        Integer eventId = 1;
        List<Event> events = eventService.getAllEvents("");
        if(events == null || events.size() == 0 ){
            eventId = eventService.createEvent(new Event(EVENT_NAME, EVENT_DATE, null, EVENT_DESCRIPTION, EVENT_CATEGORY, EVENT_CAPACITY));
        }
        else{
            eventId = events.get(0).getId();
        }   

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        TicketsResponse response = this.restTemplate.exchange(url + "/" + eventId + "/tickets", HttpMethod.GET, entity, TicketsResponse.class).getBody();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Success", response.getMessage());
    }
}
