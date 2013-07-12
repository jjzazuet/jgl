package net.tribe7.opengl;

import static net.tribe7.common.base.Throwables.*;

import javax.media.opengl.*;

import net.tribe7.opengl.util.GLCheckError;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

import org.slf4j.*;

public abstract class GLScheduledEventListener implements GLEventListener {

	protected static final Logger log = LoggerFactory.getLogger(GLScheduledEventListener.class);
	protected final GLCheckError checkError = new GLCheckError();

	private GLViewSize glViewSize = new GLViewSize(0, 0, 0, 0);
	private ExecutionState executionState = new ExecutionState();

	protected abstract void doInit(GLAutoDrawable gad) throws Exception;
	protected abstract void doRender(GLAutoDrawable gad, ExecutionState currentState) throws Exception;
	protected abstract void doUpdate(GLAutoDrawable gad, ExecutionState currentState) throws Exception;
	protected abstract void onResize(GLAutoDrawable gad, GLViewSize newViewport);
	
	@Override
	public void init(GLAutoDrawable arg0) {
		arg0.setAutoSwapBufferMode(false);
		try { 
			doInit(arg0);
			checkError(arg0.getGL());
		} 
		catch (Exception e) { propagate(e); }
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		try {
			switch (executionState.getMethod()) {
				case RENDER: doRender(arg0, executionState); break;
				case UPDATE: doUpdate(arg0, executionState); break;
			}
			checkError(arg0.getGL());
		} catch (Exception e) { propagate(e); }
	}

	@Override
	public void dispose(GLAutoDrawable gad) { 
		log.debug("Disposing GL event listener..."); // TODO implement destructor
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
	
	protected final int getError(GL gl) {
		return checkError.get(gl);
	}

	public GLViewSize getGlViewSize() { return glViewSize; }
	public ExecutionState getExecutionState() { return executionState; }
}
