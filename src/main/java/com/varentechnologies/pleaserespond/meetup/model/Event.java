package com.varentechnologies.pleaserespond.meetup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class Event {

    // UTC start time of the event, in milliseconds since the epoch
    @JsonProperty("time")
    private long time;

    @JsonProperty("event_url")
    private String eventUrl;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }
}
