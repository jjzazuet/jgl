package net.tribe7.demos.mchochlik.t016CartoonTorus;

import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL3.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Torus;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/016_cartoon_torus.png")
public class T016CartoonTorus extends GL3EventListener {

	private GLProgram p;
	private Torus torus = new Torus(1.0, 0.5, 72, 48); // 72, 48
	private GLVertexArray torusVao = new GLVertexArray();
	private GLBuffer torusIndices;

	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Vector3 target = new Vector3();
	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 camMat = new BufferedMatrix4();
	private ModelTransform modelTransform = new ModelTransform();

	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix, uModelMatrix;

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t016CartoonTorus/cartoonTorus.vs", 
				"/net/tribe7/demos/mchochlik/t016CartoonTorus/cartoonTorus.fs", gl);

		p.bind();
		torusVao.init(gl);
		
		GLBuffer torusVerts = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("Position").set(torusVao, torusVerts, false, 0).enable();
		
		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();		
		p.getInterface().getVec3("LightPos").set(new Vector3(4.0f, 4.0f, -8.0f));

		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		uModelMatrix = p.getInterface().getMat4("ModelMatrix");

		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		getDrawHelper().glFrontFace(torus.getFaceWinding());
		gl.glCullFace(GL_BACK);
		gl.glEnable(GL_LINE_SMOOTH);
		gl.glLineWidth(4);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		torusVao.bind();
		getDrawHelper().glClearColor().glClearDepth();

		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		gl.glCullFace(GL_FRONT);
		getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());

		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		gl.glCullFace(GL_BACK);
		getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
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
		uCameraMatrix.set(camMat);
		uModelMatrix.set(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMat, fov.setDegrees(75), newViewport.width / newViewport.height, 1, 30);
		uProjectionMatrix.set(projMat);
	}
}