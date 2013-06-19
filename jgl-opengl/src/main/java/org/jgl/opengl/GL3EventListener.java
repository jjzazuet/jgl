package org.jgl.opengl;

import static java.lang.String.format;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES2.GL_SHADING_LANGUAGE_VERSION;

import javax.media.opengl.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public abstract class GL3EventListener extends GLScheduledEventListener {

	protected abstract void doInit(GL3 gl) throws Exception;
	protected abstract void doRender(GL3 gl, ExecutionState currentState) throws Exception;
	protected abstract void doUpdate(GL3 gl, ExecutionState currentState) throws Exception;
	protected abstract void onResize(GL3 gl, GLViewSize newViewport);
	
	@Override
	protected void doInit(GLAutoDrawable gad) throws Exception {
		GL3 gl = (GL3) gad.getGL();
		log.info(format("OpenGL vendor: [%s]", gl.glGetString(GL_VENDOR)));
		log.info(format("OpenGL renderer: [%s]", gl.glGetString(GL_RENDERER)));
		log.info(format("OpenGL version: [%s]", gl.glGetString(GL_VERSION)));
		log.info(format("OpenGL Shading language version: [%s]", gl.glGetString(GL_SHADING_LANGUAGE_VERSION)));
		doInit(gl);
	}

	@Override
	public void doRender(GLAutoDrawable gad, ExecutionState currentState) throws Exception {
		doRender((GL3) gad.getGL(), currentState);
		gad.swapBuffers();
	}

	@Override
	protected void doUpdate(GLAutoDrawable gad, ExecutionState currentState) throws Exception {
		doUpdate((GL3) gad.getGL(), currentState);
	}

	@Override
	protected void onResize(GLAutoDrawable gad, GLViewSize newViewport) {
		onResize((GL3) gad.getGL(), newViewport);
	}
}
