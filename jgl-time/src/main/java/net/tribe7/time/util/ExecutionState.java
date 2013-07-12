package net.tribe7.time.util;

import net.tribe7.time.Timer;

public class ExecutionState {
	
	private StateMethod method = StateMethod.UPDATE;
	private final Timer logicTimer = new Timer();
	private final Timer renderTimer = new Timer();
	private double elapsedTimeUs, frameTimeUs, tickTimeUs, tickDelta;

	public double getElapsedTimeSeconds() {
		double time = elapsedTimeUs * 0.000001;
		return time;
	}

	public Timer getLogicTimer() { return logicTimer; }
	public Timer getRenderTimer() { return renderTimer; }

	public double getElapsedTimeUs() { return elapsedTimeUs; }
	public void setElapsedTimeUs(double elapsedTimeUs) { this.elapsedTimeUs = elapsedTimeUs; }

	public double getFrameTimeUs() { return frameTimeUs; }
	public void setFrameTimeUs(double frameTimeUs) { this.frameTimeUs = frameTimeUs; }

	public double getTickTimeUs() { return tickTimeUs; }
	public void setTickTimeUs(double tickTimeUs) { this.tickTimeUs = tickTimeUs; }

	public double getTickDelta() { return tickDelta; }
	public void setTickDelta(double tickDelta) { this.tickDelta = tickDelta; }

	public StateMethod getMethod() { return method; }
	public void setMethod(StateMethod method) { this.method = method; }
}
