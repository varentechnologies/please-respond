package com.varentechnologies.pleaserespond.meetup.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.ImmutableList;
import com.varentechnologies.pleaserespond.meetup.model.Rsvp;
import com.varentechnologies.pleaserespond.meetup.model.RsvpReport;
import com.varentechnologies.pleaserespond.meetup.registry.RsvpRegistry;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Generates a csv report using the ${@link RsvpRegistry}
 */
@Component
public class ReportGenerator {

    private final RsvpRegistry rsvpRegistry;

    public ReportGenerator(RsvpRegistry rsvpRegistry) {
        this.rsvpRegistry = rsvpRegistry;
    }

    /**
     * Generates the csv report
     * 
     * @return the csv report
     */
    public String generateOutput() throws JsonProcessingException {
        return toCSV(createRsvpReport());
    }

    private RsvpReport createRsvpReport() {
        RsvpReport rsvpReport = new RsvpReport();
        rsvpReport.setTotalRsvps(rsvpRegistry.getRsvpCount());

        Rsvp rsvpForLatestEvent = rsvpRegistry.getRsvpForLatestEvent();
        rsvpReport.setFutureDate(new Date(rsvpForLatestEvent.getEvent().getTime()));
        rsvpReport.setFutureUrl(rsvpForLatestEvent.getEvent().getEventUrl());

        List<Map.Entry<String, Integer>> countryRsvps = rsvpRegistry.getTopThreeCountryRsvps();
        rsvpReport.setCountry1(countryRsvps.get(0).getKey());
        rsvpReport.setCountry1Count(countryRsvps.get(0).getValue());

        rsvpReport.setCountry2(countryRsvps.get(1).getKey());
        rsvpReport.setCountry2Count(countryRsvps.get(1).getValue());

        rsvpReport.setCountry3(countryRsvps.get(2).getKey());
        rsvpReport.setCountry3Count(countryRsvps.get(2).getValue());

        return rsvpReport;
    }

    private String toCSV (RsvpReport rsvpReport) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(RsvpReport.class).withHeader();
        return mapper.writer(schema).writeValueAsString(ImmutableList.of(rsvpReport));
    }
}
