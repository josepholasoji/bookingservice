package com.musala.services.booking.configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.musala.services.booking.models.responses.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EventResponse.class, new ResponseDeserializer<EventResponse>(){});
        module.addDeserializer(EventsResponse.class, new ResponseDeserializer<EventsResponse>(){});
        module.addDeserializer(UserResponse.class, new ResponseDeserializer<UserResponse>(){});
        module.addDeserializer(UsersResponse.class, new ResponseDeserializer<UsersResponse>(){});
        module.addDeserializer(TicketResponse.class, new ResponseDeserializer<TicketResponse>(){});
        module.addDeserializer(TicketsResponse.class, new ResponseDeserializer<TicketsResponse>(){});
        objectMapper.registerModule(module);
        return objectMapper;
    }
}