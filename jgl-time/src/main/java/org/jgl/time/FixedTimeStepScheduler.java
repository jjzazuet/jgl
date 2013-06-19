package org.jgl.time;

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

		long newTimeMillis = getCurrentTimeUs();
		long frameTimeMillis = newTimeMillis - currentTimeUs;
		
		if (frameTimeMillis > maxFrameDeltaUs) {
			frameTimeMillis = maxFrameDeltaUs;
		}
		
		currentTimeUs = newTimeMillis;
		accumulator = accumulator + frameTimeMillis;
		
		while (accumulator >= frameDeltaUs) {
			stateListener.updateTick(t, frameDeltaUs);
			t = t + frameDeltaUs;
			accumulator = accumulator - frameDeltaUs;
		}

		stateListener.renderTick(currentTimeUs, ((double)accumulator)/((double)frameDeltaUs));
	}
	
	@Override
	public void doInit() {
		currentTimeUs = getCurrentTimeUs();
		maxFrameDeltaUs = MAX_FRAME_DELTA_SCALE_FACTOR * frameDeltaUs;
	}
}
