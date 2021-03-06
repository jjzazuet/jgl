package net.tribe7.opengl;

import static net.tribe7.time.Scheduler.*;
import static net.tribe7.common.base.Throwables.*;
import static net.tribe7.time.util.StateMethod.*;

import javax.media.opengl.*;
import net.tribe7.opengl.util.*;
import net.tribe7.time.*;
import org.slf4j.*;

public abstract class GLScheduledEventListener implements GLEventListener, RenderStateListener {

	public static final double DEFAULT_FRAME_TICKS_PER_SECOND = 60.0;

	protected static final Logger log = LoggerFactory.getLogger(GLScheduledEventListener.class);
	protected final GLCheckError checkError = new GLCheckError();

	private long debugTickCounter = 0l;
	private GLViewSize glViewSize = new GLViewSize(0, 0, 0, 0);
	private GLExecutionState executionState = new GLExecutionState();
	private final Scheduler scheduler = new FixedTimeStepScheduler();

	protected abstract void doInit(GLAutoDrawable gad) throws Exception;
	protected abstract void doRender(GLExecutionState currentState) throws Exception;
	protected abstract void doUpdate(GLExecutionState currentState) throws Exception;
	protected abstract void onResize(GLAutoDrawable gad, GLViewSize newViewport);

	@Override
	public void init(GLAutoDrawable arg0) {

		arg0.setAutoSwapBufferMode(false);

		if (scheduler.getFrameTicksPerSecond() == MINUS_ONE) {
			scheduler.setFrameTicksPerSecond(DEFAULT_FRAME_TICKS_PER_SECOND);
			log.info("Target framerate not set. Using default: [{}]", 
					scheduler.getFrameTicksPerSecond());
		}
		scheduler.setStateListener(this);

		try { 
			doInit(arg0);
			checkError(arg0.getGL());
		} catch (Exception e) { propagate(e); }
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		try {
			executionState.setContext(arg0);
			scheduler.stateTick();
			checkError(arg0.getGL());
		} catch (Exception e) { propagate(e); }
	}

	@Override
	public void updateTick(double elapsedTimeUs, double frameTimeUs) throws Exception {

		if (log.isTraceEnabled()) {
			debugTickCounter++;
			if (debugTickCounter % 120 == 0) {
				log.info("U:[{}]", getExecutionState().getLogicTimer().getFps());
				log.info("R:[{}]", getExecutionState().getRenderTimer().getFps());
			}
		}

		executionState.getLogicTimer().update();
		executionState.setMethod(UPDATE);
		executionState.setElapsedTimeUs(elapsedTimeUs);
		executionState.setFrameTimeUs(frameTimeUs);
		doUpdate(executionState);
	}

	@Override
	public void renderTick(double tickTimeUs, double tickDelta) throws Exception {
		executionState.getRenderTimer().update();
		executionState.setMethod(RENDER);
		executionState.setTickTimeUs(tickTimeUs);
		executionState.setTickDelta(tickDelta);
		doRender(executionState);
	}

	@Override
	public void dispose(GLAutoDrawable gad) { 
		log.debug("Disposing GL event listener..."); // TODO implement destructors
	}

	@Override
	public void reshape(GLAutoDrawable gad, int x, int y, int w, int h) {
		glViewSize = new GLViewSize(x, y, w, h);
		onResize(gad, glViewSize);
		checkError(gad.getGL());
	}

	protected final void checkError(GL gl) {
		if (log.isDebugEnabled()) {
			checkError.apply(gl);
		}
	}

	public GLViewSize getGlViewSize() { return glViewSize; }
	public Scheduler getScheduler() { return scheduler; }
	public GLExecutionState getExecutionState() { return executionState; }
}