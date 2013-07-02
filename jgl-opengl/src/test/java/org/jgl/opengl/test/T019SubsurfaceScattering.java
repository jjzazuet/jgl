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
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T019SubsurfaceScattering extends GL3EventListener {

	private int instructionCount = 32;
	private Cube cube = new Cube();
	private ModelTransform cubeTransform = new ModelTransform();
	
	private GLProgram p;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVertices;
	private GLUInt uFrontFacing;
	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	
	private BufferedMatrix4 camMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private Vector3 camTarget = new Vector3();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Angle fov = new Angle().setDegrees(70);

	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t019SubsurfaceScattering/subsurfaceScattering.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t019SubsurfaceScattering/subsurfaceScattering.fs", gl);
	
		cubeVao.init(gl);
		p.bind();
		
		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
		p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();

		p.getVec3("LightPos").set(-3.0f, -2.0f, -3.0f);
		p.getInt("InstCount").set(instructionCount);
		
		uFrontFacing = p.getInt("FrontFacing");
		uModelMatrix = p.getMat4("ModelMatrix");
		uCameraMatrix = p.getMat4("CameraMatrix");
		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		
		gl.glClearColor(0.5f, 0.6f, 0.5f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		glFrontFace(gl, cube.getFaceWinding());
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		cubeVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glCullFace(GL_FRONT);
		uFrontFacing.set(0);
		gl.glDrawArraysInstanced(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity(), instructionCount);
		gl.glCullFace(GL_BACK);
		uFrontFacing.set(1);
		gl.glDrawArraysInstanced(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity(), instructionCount);
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		double time = currentState.getElapsedTimeSeconds();
		orbit(camMatrix, camTarget, 3, azimuth.setDegrees(time * 50), 
				elevation.setDegrees(sineWave(time / 16.0) * 80));
		uCameraMatrix.setColMaj(camMatrix);
		cubeTransform.getRotationY().setDegrees(time * 25);
		uModelMatrix.setColMaj(cubeTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		glViewPort(gl, newViewport);
		perspectiveX(projMatrix, fov, newViewport.aspectRatio, 1, 20);
		uProjectionMatrix.setColMaj(projMatrix);
	}
}
