package org.jgl.time.util;

import org.jgl.time.Timer;

public class ExecutionState {
	
	public StateMethod method = StateMethod.UPDATE;
	public Timer logicTimer = new Timer();
	public Timer renderTimer = new Timer();
	public double elapsedTimeUs, frameTimeUs, tickTimeUs, tickDelta;
	
	public double getElapsedTimeSeconds() {
		double time = elapsedTimeUs * 0.000001;
		return time;
	}
}
