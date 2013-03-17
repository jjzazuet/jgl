package org.jgl.time.test;

import org.jgl.time.Timer;
import org.junit.Test;

public class TimerTest {

	public final String TIMER_INFO_FORMAT = "[%s] currentUs: [%s], deltaUs: [%s], totalUs [%s], fps: [%.8f]";
	
	@Test
	public void testTimer() throws Exception {
		
		int sleepTimeMillis = 100;
		int iterations = 240;
		
		Timer lt = new Timer();
		
		for (int k = 0; k < iterations; k++) {
			lt.update();
			System.err.println(formatTime(lt));
			Thread.sleep(sleepTimeMillis);
		}
	}
	
	public String formatTime(Timer t) {
		return String.format(TIMER_INFO_FORMAT, 
				"lt",
				t.getCurrentTimeUs(),
				t.getElapsedMillis(),
				t.getElapsedSecondsSinceStart(),
				t.getFps());		
	}
}
