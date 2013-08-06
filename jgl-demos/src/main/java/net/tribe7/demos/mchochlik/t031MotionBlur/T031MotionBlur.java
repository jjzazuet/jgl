package net.tribe7.demos.mchochlik.t031MotionBlur;

import javax.media.opengl.GL3;

import net.tribe7.opengl.GL3EventListener;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T031MotionBlur extends GL3EventListener {

	private DrawProgram drawProg = new DrawProgram();

	@Override
	protected void doInit(GL3 gl) throws Exception {
		initResource(gl, drawProg);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		// TODO Auto-generated method stub

	}

}
