package net.tribe7.opengl;

import static java.lang.String.format;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES2.GL_SHADING_LANGUAGE_VERSION;

import java.io.PrintStream;
import java.lang.reflect.Constructor;

import javax.media.opengl.*;

import net.tribe7.opengl.util.GLDrawHelper;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public abstract class GL3EventListener extends GLScheduledEventListener {

	private GLDrawHelper drawHelper = new GLDrawHelper();
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

		if (log.isDebugEnabled()) {
			Class<?> traceGlClass = Class.forName("javax.media.opengl.TraceGL3");
			Constructor<?> c = traceGlClass.getConstructor(GL3.class, PrintStream.class);
			GL3 traceGl = (GL3) c.newInstance(gl, System.err);
			gad.setGL(traceGl);
			gl = traceGl;
			log.info(format("Using TraceGL implementation: [%s]", gad.getGL().getClass().getCanonicalName()));
		} else {
			log.info(format("Using default GL implementation [%s]", gad.getGL().getClass().getCanonicalName()));
		}
		getDrawHelper().init(gl);
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

	public GLDrawHelper getDrawHelper() { return drawHelper; }
}
