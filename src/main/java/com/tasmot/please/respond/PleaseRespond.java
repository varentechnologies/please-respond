package com.tasmot.please.respond;

import org.apache.logging.log4j.*;

import java.util.concurrent.*;


public class PleaseRespond {
	private static final Logger logger = LogManager.getLogger("PleaseRespond.class");

	public static final String POISON_PILL = String.valueOf((Integer.MAX_VALUE));

	public static void main(String[] args) throws
	                                       Exception {
		int secondsToRun = processArgs(args);

		BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
		try {
			GetMeetupRsvps getMeetupRsvps = new GetMeetupRsvps(secondsToRun, blockingQueue);
			Thread getMeetupRsvpsThread = new Thread(getMeetupRsvps, "Get Meetup RSVPs");
			getMeetupRsvpsThread.start();

			ProcessMeetupRsvps processMeetupRsvps = new ProcessMeetupRsvps(blockingQueue);
			Thread processRsvpsThread = new Thread(processMeetupRsvps, "Process Meetup RSVPs");
			processRsvpsThread.start();

			getMeetupRsvpsThread.join();
			logger.debug("The '{}' thread has ended", getMeetupRsvpsThread.getName());
			processRsvpsThread.join();

			logger.debug("The '{}' thread has ended", processRsvpsThread.getName());

		} catch (InterruptedException e) {
			logger.info("Interrupted Exception");
			Thread.currentThread().interrupt();
		}
		System.exit(0);
	}

	static Integer processArgs(String[] args) {

		int secondsIn;
		if (args.length == 1) {
			try {
				secondsIn = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				logger.info(" ");
				logger.info(" ");
				logger.warn(
						"ERROR The number of seconds to run was not a valid number '{}' defaulting to 60",
						args[0]
				           );
				logger.info("   Syntax (typical) 'java please-respond-1.0-SNAPSHOT.jar ##  ");
				logger.info("   Please replace the ## with a number of seconds to execute");
				logger.info(" ");
				logger.info(" ");
				secondsIn = 60;
			}
		} else {
			secondsIn = 60;  //if no parameter was passed then default to 60
		}
		logger.info("Number of seconds to run: {}", secondsIn);
		return secondsIn;
	}
}
