package net.tribe7.opengl;

import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import javax.media.opengl.*;
import net.tribe7.opengl.util.*;
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
		log.info("OS Name, arch: [{}][{}]", System.getProperty("os.name"), System.getProperty("os.arch"));
		log.info("OpenGL vendor: [{}]", gl.glGetString(GL_VENDOR));
		log.info("OpenGL renderer: [{}]", gl.glGetString(GL_RENDERER));
		log.info("OpenGL version: [{}]", gl.glGetString(GL_VERSION));
		log.info("OpenGL Shading language version: [{}]", gl.glGetString(GL_SHADING_LANGUAGE_VERSION));

		if (log.isTraceEnabled()) {
			Class<?> traceGlClass = Class.forName("javax.media.opengl.TraceGL3");
			Constructor<?> c = traceGlClass.getConstructor(GL3.class, PrintStream.class);
			GL3 traceGl = (GL3) c.newInstance(gl, System.err);
			gad.setGL(traceGl);
			gl = traceGl;
		}

		log.info("GL implementation: [{}]", gad.getGL().getClass().getCanonicalName());
		getDrawHelper().setGl(gl);
		doInit(gl);
	}

	@Override
	public void doRender(GLExecutionState currentState) throws Exception {
		doRender((GL3) currentState.getContext().getGL(), currentState);
		currentState.getContext().swapBuffers();
	}

	@Override
	protected void doUpdate(GLExecutionState currentState) throws Exception {
		doUpdate((GL3) currentState.getContext().getGL(), currentState);
	}

	@Override
	protected void onResize(GLAutoDrawable gad, GLViewSize newViewport) {
		onResize((GL3) gad.getGL(), newViewport);
	}

	public void initResource(GL3 gl, GLContextBoundResource ... r) {
		checkNotNull(r);
		for (GLContextBoundResource res : r) {
			res.init(gl);
		}
	}

	public GLDrawHelper getDrawHelper() { return drawHelper; }
}
