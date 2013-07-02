package org.jgl.opengl.test;

import static org.jgl.math.angle.AngleOps.sineWave;
import static org.jgl.math.matrix.Matrix4OpsCam.orbit;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static javax.media.opengl.GL.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Cube;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUFloatMat4;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T014MultiCubeGs extends GL3EventListener {

	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Vector3 origin = new Vector3();
	private Cube cube = new Cube();
	private GLProgram p;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix;
	private GLBuffer cubeVertices;
	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 camMat = new BufferedMatrix4();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t014MultiCubeGs/multiCube.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t014MultiCubeGs/multiCube.gs",
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t014MultiCubeGs/multiCube.fs", gl);

		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		uCameraMatrix = p.getMat4("CameraMatrix");

		cubeVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();

		gl.glClearColor(0.9f, 0.9f, 0.9f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		cubeVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();
		
		azimuth.setDegrees(time * 135);
		elevation.setDegrees(sineWave(time / 20) * 30);
		orbit(camMat, origin, 18.5, azimuth, elevation);
		uCameraMatrix.setColMaj(camMat);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
		perspectiveX(projMat, fov.setDegrees(70), newViewport.width / newViewport.height, 1, 50);
		uProjectionMatrix.setColMaj(projMat);		
	}
}
