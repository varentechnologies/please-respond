package com.varentechnologies.pleaserespond.meetup.service;

import com.varentechnologies.pleaserespond.meetup.handler.MeetupWebsocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;

@Component
public class MeetupService {

    private final MeetupWebsocketHandler meetupWebsocketHandler;

    private final String websocketEndpoint;

    public MeetupService(MeetupWebsocketHandler meetupWebsocketHandler,
                         @Value("${websocket.endpoint}") String websocketEndpoint) {
        this.meetupWebsocketHandler = meetupWebsocketHandler;
        this.websocketEndpoint = websocketEndpoint;
    }

    public void initializeNewClientConnection(int lifetimeSeconds) {
        var webSocketClient = new StandardWebSocketClient();

        this.clientSession = webSocketClient.doHandshake(this, new WebSocketHttpHeaders(),
                URI.create("wss://echo.websocket.org")).get();
    }
}
