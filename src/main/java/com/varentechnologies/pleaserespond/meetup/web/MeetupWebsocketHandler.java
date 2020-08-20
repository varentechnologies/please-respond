package com.varentechnologies.pleaserespond.meetup.web;

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
    
    private RsvpRegistry rsvpRegistry;
    
    public MeetupWebsocketHandler(RsvpRegistry rsvpRegistry) {
        this.rsvpRegistry = rsvpRegistry;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOG.info("Received message: {}", message.getPayload());
    }
}
