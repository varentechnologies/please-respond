package com.tasmot.please.respond;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static com.tasmot.please.respond.PleaseRespond.*;

public class ProcessMeetupRsvps
		implements Runnable {
	private static final Logger logger = LogManager.getLogger(ProcessMeetupRsvps.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	BlockingQueue<String> blockingQueue;

	//Results

	int rsvpCnt = 0;
	Timestamp mostFutureEvent = new Timestamp(System.currentTimeMillis());
	String mostFutureUrl = "";
	Map<String, Integer> rspvsByCountry = new HashMap<>();

	public ProcessMeetupRsvps(BlockingQueue<String> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	public void run() {
		try {
			processRsvps();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processRsvps() throws
	                            InterruptedException {

		logger.info("Starting in ProcessRsvps");

		JsonNode jsonNode;
		String anRsvp = null;
		String countryCode;
		try {
			anRsvp = blockingQueue.take();          //priming queue read
			while (!(POISON_PILL.equals(anRsvp))) {
				logger.trace("From the Queue: {}", anRsvp);
				rsvpCnt++;
				jsonNode = mapper.readTree(anRsvp);
				Timestamp eventTime = new Timestamp(jsonNode.get("event").get("time").asLong());
				if (eventTime.after(mostFutureEvent)) {
					mostFutureEvent = eventTime;
					mostFutureUrl = jsonNode.get("event").get("event_url").asText();
				}

				countryCode = jsonNode.get("group").get("group_country").asText();

				rspvsByCountry.put(countryCode, rspvsByCountry.containsKey(countryCode) ?
						rspvsByCountry.get(countryCode) + 1 : 1);
				anRsvp = blockingQueue.take();  //get next queue message
			}
		} catch (InterruptedException e) {
			logger.debug("Received InterruptedException Exception");
			Thread.currentThread().interrupt();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			System.exit(55);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.exit(56);
		}
		rsvpResults();
	}

	public void rsvpResults() {
		StringBuilder resultsBuilder = new StringBuilder(120).append(rsvpCnt).append(",");
		resultsBuilder.append(mostFutureEvent).append(",").append(mostFutureUrl);

		Map<String, Integer> sortedRspvsByCountry = rspvsByCountry.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(3)
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new
				                         )
				        );

		for (Map.Entry<String, Integer> country : sortedRspvsByCountry.entrySet()) {
			resultsBuilder.append(",").append(country.getKey());
			resultsBuilder.append(",").append(country.getValue());
		}

		logger.debug("");
		logger.debug("");
		logger.debug("");
		logger.info("");
		logger.info("");
		logger.info("{}", resultsBuilder);
		logger.info("");
		logger.info("");

	}
}
