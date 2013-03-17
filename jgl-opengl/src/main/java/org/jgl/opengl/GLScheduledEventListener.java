package org.jgl.opengl;

import static com.google.common.base.Throwables.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static java.lang.String.format;

import javax.media.opengl.*;

import org.jgl.opengl.util.GlCheckError;
import org.jgl.time.util.ExecutionState;
import org.slf4j.*;

public abstract class GLScheduledEventListener implements GLEventListener {

	protected static final Logger log = LoggerFactory.getLogger(GLScheduledEventListener.class);
	protected final GlCheckError checkError = new GlCheckError();

	private GlViewSize glViewSize = new GlViewSize(0, 0, 0, 0);
	private ExecutionState executionState = new ExecutionState();

	protected abstract void doInit(GLAutoDrawable gad);
	public abstract void doRender(GLAutoDrawable gad, ExecutionState currentState) throws Exception;
	protected abstract void doUpdate(GLAutoDrawable gad, ExecutionState currentState) throws Exception;
	
	@Override
	public void init(GLAutoDrawable arg0) {

		GL3 gl = (GL3) arg0.getGL();

		log.info(format("OpenGL vendor: [%s]", gl.glGetString(GL_VENDOR)));
		log.info(format("OpenGL renderer: [%s]", gl.glGetString(GL_RENDERER)));
		log.info(format("OpenGL version: [%s]", gl.glGetString(GL_VERSION)));
		log.info(format("OpenGL Shading language version: [%s]", gl.glGetString(GL_SHADING_LANGUAGE_VERSION)));
		
		doInit(arg0);
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
		log.info("Disposing GL event listener..."); // TODO implement destructor
	}

	@Override
	public void reshape(GLAutoDrawable gad, int x, int y, int w, int h) {
		glViewSize = new GlViewSize(x, y, w, h);
		onResize(gad, glViewSize);
	}
	
	protected final void checkError(GL gl) {
		if (log.isDebugEnabled()) {
			checkError.apply(gl);
		}
	}

	protected abstract void onResize(GLAutoDrawable gad, GlViewSize newViewport);
	
	public GlViewSize getGlViewSize() { return glViewSize; }
	public ExecutionState getExecutionState() { return executionState; }
}
