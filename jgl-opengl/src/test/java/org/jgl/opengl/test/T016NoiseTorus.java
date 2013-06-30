package org.jgl.opengl.test;

import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Torus;
import org.jgl.math.angle.Angle;
import org.jgl.opengl.GL3EventListener;
import org.jgl.opengl.GLBuffer;
import org.jgl.opengl.GLTexture2D;
import org.jgl.opengl.GLVertexArray;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T016NoiseTorus extends GL3EventListener {

	private Torus torus = new Torus();

	private GLProgram p;
	private GLVertexArray torusVao = new GLVertexArray();
	private GLBuffer torusIndices;
	private GLTexture2D noiseTexture = new GLTexture2D();
	
	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016NoiseTorus/noiseTorus.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016NoiseTorus/noiseTorus.fs", gl);
		
		p.bind();
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
