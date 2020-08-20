package com.varentechnologies.pleaserespond.meetup.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varentechnologies.pleaserespond.meetup.model.Rsvp;
import com.varentechnologies.pleaserespond.meetup.registry.RsvpRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MeetupWebsocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MeetupWebsocketHandler.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    private final RsvpRegistry rsvpRegistry;
    
    public MeetupWebsocketHandler(RsvpRegistry rsvpRegistry) {
        this.rsvpRegistry = rsvpRegistry;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        LOG.info("Received message: {}", message.getPayload());

        Rsvp rsvp = OBJECT_MAPPER.readValue(message.getPayload(), Rsvp.class);

        rsvpRegistry.addRsvp(rsvp);
    }
}
