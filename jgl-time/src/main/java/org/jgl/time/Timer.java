package org.jgl.time;

import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.TimeUnit;

/**
 * Time is measured in {@link TimeUnit#MICROSECONDS}
 * @author jjzazuet
 */
public class Timer {

	public static final long TIMER_RESOLUTION = 1000000l;
	
	public static final double ALPHA = .25;
	public static final double BETA = 1.0 - ALPHA;	
	
	private double fpsWeightFactor1 = 0.0d;
	private double fpsWeightFactor2 = 0.0d;
	
	private double averageFramesPerSecond = 1.0;
	
	private long startTimeUs = 0l;
	private long currentDeltaUs = 0l;
	private long previousDeltaUs = 0l;
	
	private long t0 = 0l;
	private long t1 = 0l;
	
	public void update() {
		
		long timeUs = MICROSECONDS.convert(System.nanoTime(), NANOSECONDS) ;
		
		if (startTimeUs == 0l) {
			startTimeUs = timeUs;
			t0 = timeUs;
			t1 = timeUs;
		}
		
		previousDeltaUs = currentDeltaUs;
		t0 = t1;
		t1 = timeUs;
		currentDeltaUs = t1 - t0;
		
		fpsWeightFactor1 = (double) currentDeltaUs * BETA;
		fpsWeightFactor2 = (double) previousDeltaUs * ALPHA;
		averageFramesPerSecond = TIMER_RESOLUTION / (fpsWeightFactor1 + fpsWeightFactor2);
	}
	
	public long getCurrentTimeUs() { return t1; }
	public long getElapsedSecondsSinceStart() { return t1 - startTimeUs; }
	public long getElapsedMillis() { return currentDeltaUs; }
	public double getFps() { return averageFramesPerSecond; }
}
