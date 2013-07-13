package net.tribe7.opengl;

import javax.media.opengl.GLAutoDrawable;
import net.tribe7.time.util.ExecutionState;

public class GLExecutionState extends ExecutionState {

	private GLAutoDrawable context;

	public GLAutoDrawable getContext() { return context; }
	public void setContext(GLAutoDrawable context) { this.context = context; }
}
