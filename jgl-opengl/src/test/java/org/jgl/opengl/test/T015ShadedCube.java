package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;

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

public class T015ShadedCube extends GL3EventListener {

	private Cube cube = new Cube();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVertices, cubeNormals;
	private GLProgram p;
	
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix;
	private BufferedMatrix4 camMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Vector3 orbitTarget = new Vector3();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t015ShadedCube/shadedCube.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t015ShadedCube/shadedCube.fs", gl);

		p.bind();
		cubeVao.init(gl);
		
		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		uCameraMatrix = p.getMat4("CameraMatrix");
		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		
		p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
		p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();

		gl.glClearColor(0.03f, 0.03f, 0.03f, 0.0f);
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
		orbit(camMatrix, orbitTarget, 3, 
				azimuth.setDegrees(time * 135), 
				elevation.setDegrees(sineWave(time / 20) * 90));
		uCameraMatrix.setColMaj(camMatrix);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		glViewPort(gl, newViewport);
		perspectiveX(projMatrix, fov.setDegrees(60), newViewport.width / newViewport.height, 1, 20);
		uProjectionMatrix.setColMaj(projMatrix);
	}
}