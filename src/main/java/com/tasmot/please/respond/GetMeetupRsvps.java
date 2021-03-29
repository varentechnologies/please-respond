package com.tasmot.please.respond;

import org.apache.logging.log4j.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.time.*;
import java.util.concurrent.*;

import static java.lang.System.currentTimeMillis;

public class GetMeetupRsvps
		implements AutoCloseable,
		           Runnable {
	private static final Logger logger = LogManager.getLogger(GetMeetupRsvps.class);

	private URL meetupRsvpURL;

	private static BufferedReader rsvpIn;
	private static String initialUrl = "http://stream.meetup.com/2/rsvps";
	private static String desiredFields = "?order=time&only=event.time,event.event_url,group.group_country";
	private static String urlPlusQuery = initialUrl + desiredFields;

	private int secondsToRead;
	BlockingQueue<String> blockingQueue;

	public GetMeetupRsvps(Integer secondsToRead, BlockingQueue<String> blockingQueue) throws
	                                                                                  Exception {
		try {
			meetupRsvpURL = new URL(urlPlusQuery);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.error("URL is incorrectly formatted");
			throw new Exception("URL is incorrectly formatted");
		}
		openUrlConnection(meetupRsvpURL);

		this.secondsToRead = secondsToRead;
		this.blockingQueue = blockingQueue;
	}

	private static URL openUrlConnection(URL url) throws
	                                              Exception {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setInstanceFollowRedirects(true);
			if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
					|| connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				logger.warn("I got one of the Moved bad boys: '{}, {} '", connection.getResponseCode(),
						connection.getResponseMessage()
				           );

				String newLocation = connection.getHeaderField("Location");
				if (newLocation.startsWith("/")) {
					newLocation = url.getProtocol() + "://" + url.getHost() + newLocation + desiredFields;
				}
				logger.warn("New URL is: {}", newLocation);
				return openUrlConnection(new URL(newLocation));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Creating the buffered Stream Failed");
		}
		rsvpIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		return url;
	}

	@Override
	public void run() {
		logger.info("Starting in GetMeetupRspvs");
		try {
			getRsvps(secondsToRead, blockingQueue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("At the end of GetMeetupRspvs");
	}

	public void getRsvps(Integer secondsToRead,
	                     BlockingQueue<String> blockingQueue
	                    )
			throws
			Exception {

		LocalDateTime timestampEnd =
				(new Timestamp(currentTimeMillis()).toLocalDateTime().plusSeconds(secondsToRead));
		String endTimeString = timestampEnd.toString();
		Timestamp timestamp = new Timestamp(currentTimeMillis());

		logger.info("The collection start time is: {} and the end time is: {}",
				timestamp.toLocalDateTime(),
				endTimeString
		           );

			while (timestampEnd.isAfter(new Timestamp(System.currentTimeMillis()).toLocalDateTime())) {
				try {
					String anRsvp = rsvpIn.readLine();
					blockingQueue.put(anRsvp);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("Failed trying to get a line from the URL");
					throw new Exception("Failed trying to get a line from the URL");
				} catch (InterruptedException e) {
					logger.debug("I Caught an InterruptedException");
					throw e;
				}
			}

		blockingQueue.put(PleaseRespond.POISON_PILL);

		logger.info("Data collection is Complete");
	}

	@Override
	public void close() throws
	                    Exception {
		rsvpIn.close();
	}
}
