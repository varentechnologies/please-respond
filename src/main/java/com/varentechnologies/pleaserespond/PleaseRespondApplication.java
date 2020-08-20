package com.varentechnologies.pleaserespond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PleaseRespondApplication {

	private static final Disposable disposable;

	public static void main(String[] args) {
		SpringApplication.run(PleaseRespondApplication.class, args);
	}

	@ShellMethod("Send one request. Many responses (stream) will be printed.")
	public void stream() {
		log.info("\nRequest-Stream. Sending one request. Waiting for unlimited responses (Stop process to quit)...");
		this.disposable = this.rsocketRequester
				.route("stream")
				.data(new Message(CLIENT, STREAM))
				.retrieveFlux(Message.class)
				.subscribe(er -> log.info("Response received: {}", er));
	}
}
