package com.musala.services.booking.configuration;

import java.io.IOException;

import java.util.*;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.musala.services.booking.Enums.EventCategories;
import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.models.Event;
import com.musala.services.booking.models.Ticket;
import com.musala.services.booking.models.User;
import com.musala.services.booking.models.responses.EventResponse;
import com.musala.services.booking.models.responses.EventsResponse;
import com.musala.services.booking.models.responses.TicketResponse;
import com.musala.services.booking.models.responses.TicketsResponse;
import com.musala.services.booking.models.responses.UserResponse;
import com.musala.services.booking.models.responses.UsersResponse;
import com.musala.services.booking.utils.DeserializerUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ResponseDeserializer<T> extends JsonDeserializer<T> {

    private Class<T> type = null;
    
    @SuppressWarnings("unchecked")
    public ResponseDeserializer() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            this.type = (Class<T>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Type parameter T must be specified");
        }
    }

    public boolean isOfType(Class<?> clazz) {
        return clazz.isAssignableFrom(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        if (isOfType(TicketResponse.class)) {
            JsonNode eventNode = node.get("data");
            TicketResponse ticketResponse = new TicketResponse(DeserializerUtil.TryIntGetValue("id", eventNode, 0),
                                        DeserializerUtil.TryIntGetValue("eventId", eventNode, 0),
                                        DeserializerUtil.TryIntGetValue("userId", eventNode, 0),
                                        new Date(DeserializerUtil.TryLongGetValue("bookedOn", eventNode, 0)));
            return (T) ticketResponse;
        } else if (isOfType(UserResponse.class)) {
            JsonNode eventNode = node.get("data");
            UserResponse userResponse = new UserResponse( new User(DeserializerUtil.TryIntGetValue("id", eventNode, 0),
                                 DeserializerUtil.TryStringGetValue("name", eventNode, ""),
                                 DeserializerUtil.TryStringGetValue("email", eventNode, ""),
                                 DeserializerUtil.TryStringGetValue("password", eventNode, ""),
                                 DeserializerUtil.TryStringGetValue("role", eventNode, UserCategories.User.toString()),
                                 new Date(DeserializerUtil.TryLongGetValue("createdOn", eventNode, 0)),
                                 new Date(DeserializerUtil.TryLongGetValue("updatedOn", eventNode, 0)),
                                 DeserializerUtil.TryBooleanGetValue("isActive", eventNode, false)));
            return (T) userResponse;
        } else if (isOfType(EventResponse.class)) {
            JsonNode eventNode = node.get("data");
            EventResponse eventResponse = new EventResponse(new Event(DeserializerUtil.TryIntGetValue("id", eventNode, 0),
                                    DeserializerUtil.TryStringGetValue("name", eventNode, ""),
                                    new Date(DeserializerUtil.TryLongGetValue("date", eventNode, 0)),
                                    new Date(DeserializerUtil.TryLongGetValue("enDate", eventNode, 0)),
                                    DeserializerUtil.TryStringGetValue("description", eventNode, ""),
                                    DeserializerUtil.TryStringGetValue("category", eventNode, EventCategories.Concert.toString()),
                                    DeserializerUtil.TryIntGetValue("availableSpaceForAttendees", eventNode, 0),
                                    new Date(DeserializerUtil.TryLongGetValue("createdOn", eventNode, 0)),
                                    new Date(DeserializerUtil.TryLongGetValue("updatedOn", eventNode, 0)),
                                    DeserializerUtil.TryBooleanGetValue("isActive", eventNode, false)));

            return (T) eventResponse;
        } else if (isOfType(TicketsResponse.class)) {
            List<Ticket> tickets = new ArrayList<>();
            if (node.get("data").isArray()) {
                for (JsonNode eventNode : node.get("data")) {
                    Ticket ticket = new Ticket(DeserializerUtil.TryIntGetValue("id", eventNode, 0),
                            DeserializerUtil.TryIntGetValue("eventId", eventNode, 0),
                            DeserializerUtil.TryIntGetValue("userId", eventNode, 0),
                            new Date(DeserializerUtil.TryLongGetValue("bookedOn", eventNode, 0)));
                    tickets.add(ticket);
                }
            }
            return (T) new TicketsResponse(tickets);
        } else if (isOfType(EventsResponse.class)) {
            List<Event> events = new ArrayList<>();
            if (node.get("data").isArray()) {
                for (JsonNode eventNode : node.get("data")) {
                    Event event = new Event(DeserializerUtil.TryIntGetValue("id", eventNode, 0),
                            DeserializerUtil.TryStringGetValue("name", eventNode, ""),
                            new Date(DeserializerUtil.TryLongGetValue("date", eventNode, 0)),
                            new Date(DeserializerUtil.TryLongGetValue("enDate", eventNode, 0)),
                            DeserializerUtil.TryStringGetValue("description", eventNode, ""),
                            DeserializerUtil.TryStringGetValue("category", eventNode, EventCategories.Concert.toString()),
                            DeserializerUtil.TryIntGetValue("availableSpaceForAttendees", eventNode, 0),
                            new Date(DeserializerUtil.TryLongGetValue("createdOn", eventNode, 0)),
                            new Date(DeserializerUtil.TryLongGetValue("updatedOn", eventNode, 0)),
                            DeserializerUtil.TryBooleanGetValue("isActive", eventNode, false));
                    events.add(event);
                }
            }
            return (T) new EventsResponse(events);
        } else if (isOfType(UsersResponse.class)) {
            List<User> users = new ArrayList<>();
            if (node.get("data").isArray()) {
                for (JsonNode eventNode : node.get("data")) {
                    User user = new User(DeserializerUtil.TryIntGetValue("id", eventNode, 0),
                            DeserializerUtil.TryStringGetValue("name", eventNode, ""),
                            DeserializerUtil.TryStringGetValue("email", eventNode, ""),
                            DeserializerUtil.TryStringGetValue("password", eventNode, ""),
                            DeserializerUtil.TryStringGetValue("role", eventNode, UserCategories.User.toString()),
                            new Date(DeserializerUtil.TryLongGetValue("createdOn", eventNode, 0)),
                            new Date(DeserializerUtil.TryLongGetValue("updatedOn", eventNode, 0)),
                            DeserializerUtil.TryBooleanGetValue("isActive", eventNode, false));
                    users.add(user);
                }
            }
            return (T) new UsersResponse(users);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
