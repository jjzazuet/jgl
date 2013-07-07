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
		log.info(format("OS Name, arch: [%s][%s]", System.getProperty("os.name"), System.getProperty("os.arch")));
		log.info(format("OpenGL vendor: [%s]", gl.glGetString(GL_VENDOR)));
		log.info(format("OpenGL renderer: [%s]", gl.glGetString(GL_RENDERER)));
		log.info(format("OpenGL version: [%s]", gl.glGetString(GL_VERSION)));
		log.info(format("OpenGL Shading language version: [%s]", gl.glGetString(GL_SHADING_LANGUAGE_VERSION)));

		try {
			Class.forName("javax.media.opengl.TraceGL3");
			TraceGL3 tgl3 = new TraceGL3(gl, System.err);
			gad.setGL(tgl3);
			gl = tgl3;
			log.info(format("Using TraceGL implementation: [%s]", gad.getGL().getClass().getCanonicalName()));
		} catch (Exception e) {
			log.info(format("Using default GL implementation [%s]", gad.getGL().getClass().getCanonicalName()));
		}
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
