package net.tribe7.demos.mchochlik.t031MotionBlur;

import javax.media.opengl.GL3;

import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.opengl.GL3EventListener;
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T031MotionBlur extends GL3EventListener {

	private final DrawProgram drawProg = new DrawProgram();
	private final BlurProgram blurProg = new BlurProgram();
	private final MatrixInstances instances = new MatrixInstances(256);

	// private final Cube cube = new Cube();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		initResource(gl, drawProg, blurProg);
		GLUniformBlock ub = drawProg.getModelBlock();
		GLUFloatMat4 modelMatrices = ub.getInterface().getMat4("ModelMatrices");

		ub.serialize(modelMatrices, instances.getMatrixData().toArray(new BufferedMatrix4 []{}));
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
