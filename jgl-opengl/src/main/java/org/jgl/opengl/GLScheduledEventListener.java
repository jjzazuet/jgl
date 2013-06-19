package org.jgl.opengl;

import static com.google.common.base.Throwables.*;

import javax.media.opengl.*;
import org.jgl.opengl.util.GlCheckError;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;
import org.slf4j.*;

public abstract class GLScheduledEventListener implements GLEventListener {

	protected static final Logger log = LoggerFactory.getLogger(GLScheduledEventListener.class);
	protected final GlCheckError checkError = new GlCheckError();

	private GLViewSize glViewSize = new GLViewSize(0, 0, 0, 0);
	private ExecutionState executionState = new ExecutionState();

	protected abstract void doInit(GLAutoDrawable gad) throws Exception;
	protected abstract void doRender(GLAutoDrawable gad, ExecutionState currentState) throws Exception;
	protected abstract void doUpdate(GLAutoDrawable gad, ExecutionState currentState) throws Exception;
	protected abstract void onResize(GLAutoDrawable gad, GLViewSize newViewport);
	
	@Override
	public void init(GLAutoDrawable arg0) {
		arg0.setAutoSwapBufferMode(false);
		try { doInit(arg0); } 
		catch (Exception e) { propagate(e); }
	}

	@Override
	public void display(GLAutoDrawable arg0) {		
		try {
			switch (executionState.method) {
				case RENDER: doRender(arg0, executionState); break;
				case UPDATE: doUpdate(arg0, executionState); break;
			}
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
	}

	protected final void checkError(GL gl) {
		if (log.isDebugEnabled()) {
			checkError.apply(gl);
		}
	}

	public GLViewSize getGlViewSize() { return glViewSize; }
	public ExecutionState getExecutionState() { return executionState; }
}
