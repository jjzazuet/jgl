package org.jgl.opengl.test;

import static org.jgl.opengl.util.GLDrawUtils.*;
import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Sphere;
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUFloatMat4;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T013SpiralSphere extends GL3EventListener {

	private Sphere sphere = new Sphere();
	private GLBuffer sphereIndices;
	private GLProgram p;
	private GLVertexArray sphereVao = new GLVertexArray();
	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 cameraMat = new BufferedMatrix4();

	private ModelTransform modelTransform = new ModelTransform();
	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	
	private Angle fov = new Angle();
	private Vector3 eye = new Vector3(2.5, 3.5, 2.5);
	private Vector3 target = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		GLBuffer sphereVertices = buffer(sphere.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer sphereTexCoords = buffer(sphere.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		sphereIndices = buffer(sphere.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t013SpiralSphere/spiralSphere.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t013SpiralSphere/spiralSphere.fs", gl);
		
		sphereVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(sphereVao, sphereVertices, false, 0).enable();
		p.getStageAttribute("TexCoord").set(sphereVao, sphereTexCoords, false, 0).enable();

		uCameraMatrix = p.getMat4("CameraMatrix");
		uModelMatrix = p.getMat4("ModelMatrix");
		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		
		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		sphereVao.bind();
		glIndexedDraw(GL_TRIANGLE_STRIP, sphereIndices);
		sphereVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();
		
		lookAt(cameraMat, eye, target);
		uCameraMatrix.set(cameraMat);
		modelTransform.getTranslation().setY(Math.sqrt(1 + sineWave(time / 2.0)));
		modelTransform.getRotationY().setDegrees(time * 180);
		uModelMatrix.set(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
		perspectiveX(projMat, fov.setDegrees(70), newViewport.width / newViewport.height, 1, 70);
		uProjectionMatrix.set(projMat);
	}
}