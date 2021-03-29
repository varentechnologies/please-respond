package com.tasmot.please.respond;

import org.junit.*;

import static org.junit.Assert.*;

public class ProcessArgsTest {

	@Test
	public void ProcessArgsGoodOnesTest() {

		int secondsToRun = PleaseRespond.processArgs(new String[]{"10"});
		assertTrue(
				"Should be equal",
				10 == secondsToRun
		          );
	}

	@Test
	public void ProcessInvalidArgsTest() {
		assertTrue("Should be equal", 60 == PleaseRespond.processArgs(new String[]{"Error Amount"}));
	}

}
