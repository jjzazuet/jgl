package net.tribe7.demos.mchochlik;

import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static javax.media.opengl.GL.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.geom.solid.Torus;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T017PhongTorus extends GL3EventListener {

	private Torus torus = new Torus(1.0, 0.5, 72, 48);

	private GLProgram p;
	private GLUFloatMat4 uCameraMatrix, uProjectionMatrix;
	private GLVertexArray torusVao = new GLVertexArray();
	private GLBuffer torusIndices;

	private BufferedMatrix4 camMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private Vector3 camTarget = new Vector3();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Angle fov = new Angle().setDegrees(60);

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t017PhongTorus/phongTorus.vs", 
				"/net/tribe7/demos/mchochlik/t017PhongTorus/phongTorus.fs", gl);

		p.bind();
		torusVao.init(gl);

		GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);

		p.getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();
		p.getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();

		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		uCameraMatrix = p.getMat4("CameraMatrix");

		GLUFloatVec3 lightPos = p.getVec3("LightPos");
		
		lightPos.set(0, 2.0f,-1.0f, 0.0f);
		lightPos.set(1, 0.0f, 3.0f, 0.0f);
		lightPos.set(2, 0.0f,-1.0f, 4.0f);
		
		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		getDrawHelper().glFrontFace(torus.getFaceWinding());
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		torusVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
		torusVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		double time = currentState.getElapsedTimeSeconds();
		orbit(camMatrix, camTarget, 5.0, azimuth.setDegrees(time * 135), 
				elevation.setDegrees(sineWave(time / 20.0) * 90));
		uCameraMatrix.set(camMatrix);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMatrix, fov, newViewport.aspectRatio, 1, 30);
		uProjectionMatrix.set(projMatrix);
	}
}
