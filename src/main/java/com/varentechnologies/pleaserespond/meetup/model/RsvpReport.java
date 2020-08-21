package com.varentechnologies.pleaserespond.meetup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder(value = { "totalRsvps", "futureDate", "futureUrl", "country1", "country1Count",
        "country2", "country2Count", "country3", "country3Count"})
public class RsvpReport {

    @JsonProperty("total")
    private int totalRsvps;

    @JsonProperty("future_date")
    private Date futureDate;

    @JsonProperty("future_url")
    private String futureUrl;

    @JsonProperty("co_1")
    private String country1;

    @JsonProperty("co_1_count")
    private int country1Count;

    @JsonProperty("co_2")
    private String country2;

    @JsonProperty("co_2_count")
    private int country2Count;

    @JsonProperty("co_3")
    private String country3;

    @JsonProperty("co_3_count")
    private int country3Count;

    public int getTotalRsvps() {
        return totalRsvps;
    }

    public void setTotalRsvps(int totalRsvps) {
        this.totalRsvps = totalRsvps;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public Date getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(Date futureDate) {
        this.futureDate = futureDate;
    }

    public String getFutureUrl() {
        return futureUrl;
    }

    public void setFutureUrl(String futureUrl) {
        this.futureUrl = futureUrl;
    }

    public String getCountry1() {
        return country1;
    }

    public void setCountry1(String country1) {
        this.country1 = country1;
    }

    public int getCountry1Count() {
        return country1Count;
    }

    public void setCountry1Count(int country1Count) {
        this.country1Count = country1Count;
    }

    public String getCountry2() {
        return country2;
    }

    public void setCountry2(String country2) {
        this.country2 = country2;
    }

    public int getCountry2Count() {
        return country2Count;
    }

    public void setCountry2Count(int country2Count) {
        this.country2Count = country2Count;
    }

    public String getCountry3() {
        return country3;
    }

    public void setCountry3(String country3) {
        this.country3 = country3;
    }

    public int getCountry3Count() {
        return country3Count;
    }

    public void setCountry3Count(int country3Count) {
        this.country3Count = country3Count;
    }
}
