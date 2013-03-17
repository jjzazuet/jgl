package org.jgl.time;

import static java.util.concurrent.TimeUnit.*;
import static com.google.common.base.Preconditions.*;
import java.util.concurrent.TimeUnit;

import org.jgl.time.RenderStateListener;

/**
 * Time is measured in {@link TimeUnit#MICROSECONDS}
 * @author jjzazuet
 */
public abstract class Scheduler {

	public static final long TIMER_RESOLUTION = 1000000l;
	
	protected double frameTicksPerSecond = 0.0;
	protected long currentTimeUs;
	protected long frameDeltaUs;
	
	protected double interpolatedTickDelta;	
	private boolean ready = false;
	
	protected RenderStateListener stateListener;

	/**
	 * Defines how frequently will the {@link RenderStateListener} perform simulation
	 * state logic updates and rendering.
	 * @throws Exception when an error occurs during state tick execution.
	 * @see RenderStateListener#renderTick(double, double)
	 * @see RenderStateListener#updateTick(double, double)
	 */
	public final void stateTick() throws Exception {
		if (!ready) { throw new IllegalStateException("Scheduler not initialized."); }
		doStateTick();
	}
	
	public final void init() {
		doInit();
		checkFps();
		checkListener();
		checkFrameDelta();
		ready = true;
	}

	public abstract void doStateTick() throws Exception;
	protected abstract void doInit();
	
	public void setStateListener(RenderStateListener stateListener) {
		this.stateListener = stateListener;
	}
	
	public double getFrameTicksPerSecond() {
		return frameTicksPerSecond;
	}

	public final void setFrameTicksPerSecond(double frameTicksPerSecond) {
		this.frameTicksPerSecond = frameTicksPerSecond;
		frameDeltaUs = Math.round(TIMER_RESOLUTION / frameTicksPerSecond); 
	}
	
	public long getCurrentTimeUs() {
		return MICROSECONDS.convert(System.nanoTime(), NANOSECONDS);
	}
	
	protected final void checkFps() {
		checkState(frameTicksPerSecond > 0.0, "Target framerate not set.");
	}
	
	protected final void checkFrameDelta() {
		checkState(frameDeltaUs > 0, "Target framerate delta not set.");
	}
	
	protected final void checkListener() {
		checkNotNull(stateListener, "Render state listener not set.");
	}
}
