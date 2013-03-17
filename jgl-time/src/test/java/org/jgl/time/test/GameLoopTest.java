package org.jgl.time.test;

import org.jgl.time.FixedTimeStepScheduler;
import org.jgl.time.RenderStateListener;
import org.jgl.time.Scheduler;
import org.jgl.time.Timer;
import org.junit.Test;

import com.google.common.base.Throwables;

public class GameLoopTest implements RenderStateListener {

	private Scheduler rs = new FixedTimeStepScheduler();	
	private Timer renderTimer = new Timer();
	private Timer updateTimer = new Timer();
	
	@Test
	public void start() throws Exception {
		
		rs.setFrameTicksPerSecond(4);
		rs.setStateListener(this);
		rs.init();
		
		int iterations = 360;
		int current = 0;
		
		while(current < iterations) {
			rs.stateTick();
			current++;
		}
	}

	public void updateTick(double elapsedTimeUs, double frameTimeUs) throws Exception {
		updateTimer.update();
		System.err.printf("U [%.2f, %.2f] [%.2f, %.2f]%n",
				elapsedTimeUs, frameTimeUs,
				renderTimer.getFps(), updateTimer.getFps());
	}

	public void renderTick(double tickTimeUs, double tickDelta) throws Exception {
		try {
			renderTimer.update();
			System.err.printf("R[%.2f, %.2f] ", tickTimeUs, tickDelta);
			Thread.sleep(50);
		} catch (Exception e) {
			System.err.println(Throwables.getStackTraceAsString(e));
		}	
	}
}
