package com.varentechnologies.pleaserespond.meetup.shell;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.varentechnologies.pleaserespond.PleaseRespondApplication;
import com.varentechnologies.pleaserespond.meetup.service.MeetupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.concurrent.ExecutionException;

/**
 * Enables Spring Shell's CLI functionality. Users can request stream reports of varying length
 * using the 'stream <seconds>' command
 */
@ShellComponent
public class ShellInterfaceController {

    private static final Logger LOG = LoggerFactory.getLogger(PleaseRespondApplication.class);

    private final MeetupService meetupService;

    public ShellInterfaceController(MeetupService meetupService) {
        this.meetupService = meetupService;
    }

    @ShellMethod("Request the stream and process the data for the given number of seconds before generating report" +
            " - Usage: 'stream <seconds>'")
    public void stream(int seconds) throws ExecutionException, InterruptedException, JsonProcessingException {
        LOG.info("\n{} second stream report requested", seconds);

        meetupService.openTemporaryStream(seconds);
    }
}
