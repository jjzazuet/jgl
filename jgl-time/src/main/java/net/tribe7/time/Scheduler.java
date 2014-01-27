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

	public static final double ZERO = 0.0;
	public static final double MINUS_ONE = -1.0;
	public static final long TIMER_RESOLUTION = 1000000l;

	private double frameTicksPerSecond = MINUS_ONE;
	private long currentTimeUs;
	private long frameDeltaUs;
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
		if (!isReady()) { init(); }
		doStateTick();
	}

	protected final void init() {
		doInit();
		validate();
		ready = true;
	}

	protected final void validate() {
		checkState(getFrameTicksPerSecond() > ZERO, "Invalid target framerate: [%s]", getFrameTicksPerSecond());
		checkState(getFrameDeltaUs() > ZERO, "Target framerate delta not set.");
		checkNotNull(getStateListener(), "Render state listener not set.");
		doValidate();
	}

	public final void setFrameTicksPerSecond(double frameTicksPerSecond) {
		this.frameTicksPerSecond = frameTicksPerSecond;
		frameDeltaUs = Math.round(TIMER_RESOLUTION / frameTicksPerSecond);
		setReady(false);
	}

	public long queryCurrentTimeUs() {
		return MICROSECONDS.convert(System.nanoTime(), NANOSECONDS);
	}

	public void setStateListener(RenderStateListener stateListener) {
		this.stateListener = stateListener;
	}

	public boolean isReady() { return ready; }
	public double getFrameTicksPerSecond() { return frameTicksPerSecond; }
	public long getFrameDeltaUs() { return frameDeltaUs; }
	public long getCurrentTimeUs() { return currentTimeUs; }

	public void setReady(boolean ready) { this.ready = ready; }
	public void setCurrentTimeUs(long currentTimeUs) { this.currentTimeUs = currentTimeUs; }
	public void setFrameDeltaUs(long frameDeltaUs) { this.frameDeltaUs = frameDeltaUs; }

	public RenderStateListener getStateListener() { return stateListener; }
}
