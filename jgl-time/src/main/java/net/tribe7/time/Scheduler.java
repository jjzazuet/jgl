package net.tribe7.time;

import static java.util.concurrent.TimeUnit.*;
import static net.tribe7.common.base.Preconditions.*;

import java.util.concurrent.TimeUnit;

import net.tribe7.time.RenderStateListener;

/**
 * Time is measured in {@link TimeUnit#MICROSECONDS}
 * @author jjzazuet
 */
public abstract class Scheduler {

	public static final long TIMER_RESOLUTION = 1000000l;

	protected double interpolatedTickDelta;
	protected double frameTicksPerSecond = 0.0;
	protected long currentTimeUs;
	protected long frameDeltaUs;

	private boolean ready = false;

	protected RenderStateListener stateListener;

	protected abstract void doStateTick() throws Exception;
	protected abstract void doInit();
	protected abstract void doValidate();

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
		validate();
		ready = true;
	}

	protected final void validate() {
		checkState(frameTicksPerSecond > 0.0, "Target framerate not set.");
		checkState(frameDeltaUs > 0, "Target framerate delta not set.");
		checkNotNull(stateListener, "Render state listener not set.");
		doValidate();
	}

	public final void setFrameTicksPerSecond(double frameTicksPerSecond) {
		this.frameTicksPerSecond = frameTicksPerSecond;
		frameDeltaUs = Math.round(TIMER_RESOLUTION / frameTicksPerSecond); 
	}
	
	public long getCurrentTimeUs() {
		return MICROSECONDS.convert(System.nanoTime(), NANOSECONDS);
	}

	public void setStateListener(RenderStateListener stateListener) {
		this.stateListener = stateListener;
	}

	public double getFrameTicksPerSecond() {
		return frameTicksPerSecond;
	}
}
