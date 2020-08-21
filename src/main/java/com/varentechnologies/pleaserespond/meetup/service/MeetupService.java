package com.varentechnologies.pleaserespond.meetup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.varentechnologies.pleaserespond.meetup.report.ReportGenerator;
import com.varentechnologies.pleaserespond.meetup.web.MeetupWebsocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Opens and processes the Rsvp stream.
 */
@Component
public class MeetupService {

    private static final Logger LOG = LoggerFactory.getLogger(MeetupWebsocketHandler.class);

    private final MeetupWebsocketHandler meetupWebsocketHandler;

    private final ReportGenerator reportGenerator;

    private final String websocketEndpoint;

    public MeetupService(MeetupWebsocketHandler meetupWebsocketHandler,
                         ReportGenerator reportGenerator,
                         @Value("${stream.endpoint}") String websocketEndpoint) {
        this.meetupWebsocketHandler = meetupWebsocketHandler;
        this.reportGenerator = reportGenerator;
        this.websocketEndpoint = websocketEndpoint;
    }

    /**
     * Opens and processes a temporary event stream.
     *
     * @param lifetimeSeconds the lifetime of the stream in seconds.
     */
    public void processTemporaryStream(int lifetimeSeconds) throws ExecutionException, InterruptedException,
            JsonProcessingException {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();

        WebSocketSession session = webSocketClient.doHandshake(meetupWebsocketHandler,
                new WebSocketHttpHeaders(), URI.create(websocketEndpoint)).get();

        waitForStreamExpiration(session, lifetimeSeconds);

        LOG.info("\n\n" + reportGenerator.generateOutput());
    }

    /**
     * Closes the session and generates the report after the liftime in seconds passes.
     *
     * @param session the websocket session
     * @param lifetimeSeconds the number of seconds to wait before closing the session
     */
    private void waitForStreamExpiration(WebSocketSession session, int lifetimeSeconds) throws InterruptedException,
            JsonProcessingException {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
                session.close();
            } catch (IOException ex) {
                LOG.error("Exception closing temporaryStream", ex);
            }
        }, lifetimeSeconds, TimeUnit.SECONDS);

        scheduler.awaitTermination(lifetimeSeconds, TimeUnit.SECONDS);

        LOG.info("Closed stream. Report: ");
    }
}
