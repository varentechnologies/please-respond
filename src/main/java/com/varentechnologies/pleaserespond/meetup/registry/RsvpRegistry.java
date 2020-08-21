package com.varentechnologies.pleaserespond.meetup.registry;

import com.varentechnologies.pleaserespond.meetup.model.Rsvp;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores RSVPs and offers aggregate information. Currently Storing all RSVPs but could be designed
 * to only keep running aggregates depending on expectations of future requirements.
 */
@Component
public class RsvpRegistry {

    private final Set<Rsvp> rsvps = new HashSet<>();

    public void addRsvp(Rsvp rsvp) {
        rsvps.add(rsvp);
    }

    public void clear() {
        rsvps.clear();
    }

    public int getRsvpCount() {
        return rsvps.size();
    }

    /**
     * Searches the registry for the rsvp with the latest date and returns it.
     *
     * @return the rsvp for the event with the latest date, or null if the registry is empty.
     */
    public Rsvp getRsvpForLatestEvent() {
        return rsvps.stream()
                .max(Comparator.comparingLong((rsvp) -> rsvp.getEvent().getTime()))
                .orElse(null);
    }

    /**
     * Finds the countries with the most rsvp'd events and returns them and their counts
     *
     * @return the list of countries to rsvp counts in order of most to least.
     */
    public List<Map.Entry<String, Integer>> getTopThreeCountryRsvps() {
        Map<String, Integer> countryRsvps = new HashMap<>();

        rsvps.forEach(rsvp -> {
            if (countryRsvps.containsKey(rsvp.getGroup().getCountry())) {
                countryRsvps.put(rsvp.getGroup().getCountry(), countryRsvps.get(rsvp.getGroup().getCountry()) + 1);
            } else {
                countryRsvps.put(rsvp.getGroup().getCountry(), 1);
            }
        });

        return countryRsvps.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
}
