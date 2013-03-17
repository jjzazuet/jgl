package org.jgl.time;

import java.util.concurrent.TimeUnit;

public interface RenderStateListener {
	
	/**
	 * Use this method for performing simulation state update (e.g. physics, resources, calculations, etc.).
	 * 
	 * @param elapsedTimeUs current time in {@link TimeUnit#MICROSECONDS}.
	 * @param frameTimeUs How long, in {@link TimeUnit#MICROSECONDS} is this frame.
	 * @throws Exception when an error occurs during update.
	 */
	public void updateTick(double elapsedTimeUs, double frameTimeUs) throws Exception;
	
	/**
	 * Use this method for rendering simulation state.
	 * 
	 * @param tickTimeUs current time in {@link TimeUnit#MICROSECONDS}
	 * @param tickDelta interpolated tick delta before the next frame occurs <code>[0.0, 1.0]</code>
	 * @throws Exception when an error occurs during rendering.
	 */
	public void renderTick(double tickTimeUs, double tickDelta) throws Exception;
}
