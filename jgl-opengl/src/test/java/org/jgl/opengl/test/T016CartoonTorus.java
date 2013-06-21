package org.jgl.opengl.test;

import static org.jgl.math.angle.AngleOps.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL3.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Torus;
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T016CartoonTorus extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray torusVao = new GLVertexArray();
	private GLBuffer torusIndices;
	
	private Torus torus = new Torus(1.0, 0.5, 72, 48);
	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Vector3 target = new Vector3();
	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 camMat = new BufferedMatrix4();
	private ModelTransform modelTransform = new ModelTransform();

	private GLUniformAttribute uProjectionMatrix, uCameraMatrix, uModelMatrix;

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016CartoonTorus/cartoonTorus.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016CartoonTorus/cartoonTorus.fs", gl);
		
		p.bind();
		torusVao.init(gl);
		
		GLBuffer torusVerts = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		p.getStageAttribute("Position").set(torusVao, torusVerts, false, 0).enable();
		
		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		p.getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();		
		p.getUniformAttribute("LightPos").setVec3f(new Vector3(4.0f, 4.0f, -8.0f));

		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		uProjectionMatrix = p.getUniformAttribute("ProjectionMatrix");
		uCameraMatrix = p.getUniformAttribute("CameraMatrix");
		uModelMatrix = p.getUniformAttribute("ModelMatrix");
		
		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		glFrontFace(gl, torus.getFaceWinding());
		gl.glCullFace(GL_BACK);
		gl.glEnable(GL_LINE_SMOOTH);
		gl.glLineWidth(4);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		torusVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		gl.glCullFace(GL_FRONT);
		glIndexedDraw(GL_LINE_LOOP, gl, torusIndices);

		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		gl.glCullFace(GL_BACK);
		glIndexedDraw(GL_TRIANGLES, gl, torusIndices);

		torusVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		double time = currentState.getElapsedTimeSeconds();
		orbit(camMat, target, 3.5, 
				azimuth.setDegrees(time * 35),
				elevation.setDegrees(sineWave(time / 20.0) * 60));
		modelTransform.getRotationY().setFullCircles(time * .25);
		modelTransform.getRotationX().setFullCircles(.25);
		uCameraMatrix.setMat4fv(false, camMat);
		uModelMatrix.setMat4fv(false, modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		glViewPort(gl, newViewport);
		perspectiveX(projMat, fov.setDegrees(75), newViewport.width / newViewport.height, 1, 30);
		uProjectionMatrix.setMat4fv(false, projMat);
	}
}