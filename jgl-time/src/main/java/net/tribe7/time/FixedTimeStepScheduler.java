package net.tribe7.time;

/**
 * <a href="http://gafferongames.com/game-physics/fix-your-timestep/">
 *   Fix Your Timestep (Glenn Fiedler)
 * </a>
 * @author jjzazuet
 */
public class FixedTimeStepScheduler extends Scheduler {

	public static final int MAX_FRAME_DELTA_SCALE_FACTOR = 3;

	long t = 0;
	long accumulator = 0;
	long maxFrameDeltaUs;

	@Override
	public void doStateTick() throws Exception {

		long newTimeMillis = queryCurrentTimeUs();
		long frameTimeMillis = newTimeMillis - getCurrentTimeUs();

		if (frameTimeMillis > maxFrameDeltaUs) {
			frameTimeMillis = maxFrameDeltaUs;
		}

		setCurrentTimeUs(newTimeMillis);
		accumulator = accumulator + frameTimeMillis;
		long fd = getFrameDeltaUs();

		while (accumulator >= fd) {
			stateListener.updateTick(t, fd);
			t = t + fd;
			accumulator = accumulator - fd;
		}
		stateListener.renderTick(getCurrentTimeUs(), ((double) accumulator)/((double) fd));
	}

	@Override
	public void doInit() {
		setCurrentTimeUs(queryCurrentTimeUs());
		maxFrameDeltaUs = MAX_FRAME_DELTA_SCALE_FACTOR * getFrameDeltaUs();
	}

	@Override
	protected void doValidate() {}
}
