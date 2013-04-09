package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.matrix.Matrix4OpsGeom.*;
import static org.jgl.math.angle.AngleOps.*;

import javax.media.opengl.GL3;

import org.jgl.geom.shape.Sphere;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.Matrix4;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T013SpiralSphere extends GL3EventListener {

	private Sphere sphere = new Sphere();
	private GLBuffer sphereIndices;
	private GLProgram p;
	private GLVertexArray sphereVao = new GLVertexArray();
	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 cameraMat = new BufferedMatrix4();

	private Matrix4 modelTranslationMat = new Matrix4();
	private Matrix4 modelRotationMat = new Matrix4();
	private BufferedMatrix4 modelTransformMatrix = new BufferedMatrix4();
	private GLUniformAttribute uCameraMatrix, uModelMatrix, uProjectionMatrix;
	
	private Angle fov = new Angle();
	private Angle modelRotation = new Angle();
	private Vector3 eye = new Vector3(2.5, 3.5, 2.5);
	private Vector3 target = new Vector3();
	private Vector3 modelTranslation = new Vector3();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		GLBuffer sphereVertices = buffer(sphere.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		GLBuffer sphereTexCoords = buffer(sphere.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2);
		sphereIndices = buffer(sphere.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t013SpiralSphere/spiralSphere.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t013SpiralSphere/spiralSphere.fs", gl);
		
		sphereVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(sphereVao, sphereVertices, false, 0).enable();
		p.getStageAttribute("TexCoord").set(sphereVao, sphereTexCoords, false, 0).enable();

		uCameraMatrix = p.getUniformAttribute("CameraMatrix");
		uModelMatrix = p.getUniformAttribute("ModelMatrix");
		uProjectionMatrix = p.getUniformAttribute("ProjectionMatrix");
		
		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		sphereVao.bind();
		sphereIndices.getRawBuffer().clear();
		gl.glDrawElements(GL_TRIANGLE_STRIP, 
				sphereIndices.getRawBuffer().capacity(), 
				sphereIndices.getBufferMetadata().getGlPrimitiveType(), 
				sphereIndices.getRawBuffer());
		sphereVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		
		double time = currentState.elapsedTimeUs * 0.000001;
		
		lookAt(cameraMat, eye, target);
		uCameraMatrix.setMat4fv(false, cameraMat);
		modelTranslation.setY(Math.sqrt(1 + sineWave(time / 2.0)));
		translateXyz(modelTranslationMat, modelTranslation);
		modelRotation.setDegrees(time * 180);
		rotateYLh(modelRotationMat, modelRotation);
		mul(modelTranslationMat, modelRotationMat, modelTransformMatrix);
		uModelMatrix.setMat4fv(false, modelTransformMatrix);
	}

	@Override
	protected void onResize(GL3 gl, GlViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
		perspectiveX(projMat, fov.setDegrees(70), newViewport.width / newViewport.height, 1, 70);
		uProjectionMatrix.setMat4fv(false, projMat);
	}
}