package net.tribe7.demos.mchochlik.t026StencilShadow;

import static net.tribe7.opengl.util.GLSLUtils.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4Ops.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.angle.AngleOps.*;

import javax.media.opengl.GL3;

import net.tribe7.geom.io.GeometryBuffer;
import net.tribe7.geom.solid.Torus;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T026StencilShadow extends GL3EventListener {

	private Torus torus = new Torus(1.0, 0.7, 72, 48);
	private ModelTransform modelTransform = new ModelTransform();

	private BufferedMatrix4 projectionMatrix = new BufferedMatrix4();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 identity = new BufferedMatrix4();
	private Angle fov = new Angle().setDegrees(70), azimuth = new Angle(), elevation = new Angle();
	private Vector3 cameraTarget = new Vector3();

	private GeometryBuffer<Float> planeVerts = new GeometryBuffer<Float>(3,
			new Float [] {
				-9.0f, 0.0f, -9.0f,
				-9.0f, 0.0f,  9.0f,
				 9.0f, 0.0f, -9.0f,
				 9.0f, 0.0f,  9.0f
	});

	private GeometryBuffer<Float> planeNorms = new GeometryBuffer<Float>(3, 
			new Float [] {
				-0.1f, 1.0f,  0.1f,
				-0.1f, 1.0f, -0.1f,
				 0.1f, 1.0f,  0.1f,
				 0.1f, 1.0f, -0.1f
	});

	private GLProgram objectProg, shadowProg;
	private GLVertexArray torusVao = new GLVertexArray(), planeVao = new GLVertexArray();
	private GLBuffer torusIndices;

	@Override
	protected void doInit(GL3 gl) throws Exception {

		objectProg = loadProgram(
				"/net/tribe7/demos/mchochlik/t026StencilShadow/object.vs", 
				"/net/tribe7/demos/mchochlik/t026StencilShadow/object.fs", gl);
		shadowProg = loadProgram(
				"/net/tribe7/demos/mchochlik/t026StencilShadow/shadow.vs", 
				"/net/tribe7/demos/mchochlik/t026StencilShadow/shadow.gs", 
				"/net/tribe7/demos/mchochlik/t026StencilShadow/shadow.fs", gl);

		setIdentity(identity);
		initResource(gl, torusVao, planeVao);

		Vector3 lightPosition = new Vector3(2.0f, 9.0f, 3.0f);
		GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);

		GLBuffer planeVertices = buffer(planeVerts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer planeNormals = buffer(planeNorms, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		objectProg.bind(); {
			objectProg.getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();
			objectProg.getStageAttribute("Position").set(planeVao, planeVertices, false, 0).enable();
			objectProg.getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();
			objectProg.getStageAttribute("Normal").set(planeVao, planeNormals, false, 0).enable();
			objectProg.getVec3("LightPos").set(lightPosition);
		} objectProg.unbind();

		shadowProg.bind(); {
			shadowProg.getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();
			shadowProg.getVec3("LightPos").set(lightPosition);
		} shadowProg.unbind();

		gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glClearStencil(0);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		getDrawHelper().glFrontFace(torus.getFaceWinding());
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		getDrawHelper().glClearColor().glClearDepth().glClearStencil();
		orbit(cameraMatrix, cameraTarget, 9.0, 
				azimuth.setFullCircles(time * 0.1), 
				elevation.setDegrees(15 + (-sineWave(0.25+time/12.5)+1.0)* 0.5 * 75));

		double rot = time * 0.2;
		modelTransform.getTranslation().set(0.0f, 2.5f, 0.0);
		modelTransform.getRotationX().setFullCircles(rot);
		modelTransform.getRotationY().setFullCircles(rot);
		modelTransform.getRotationZ().setFullCircles(rot);

		BufferedMatrix4 modelMatrix = modelTransform.getModelMatrix();

		gl.glCullFace(GL_BACK);
		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);
		gl.glDisable(GL_STENCIL_TEST);

		objectProg.bind(); {
			objectProg.getMat4("CameraMatrix").set(cameraMatrix);
			objectProg.getFloat("LightMult").set(0.2f);
			planeVao.bind(); {
				objectProg.getMat4("ModelMatrix").set(identity);
				gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			} planeVao.unbind();
			torusVao.bind(); {
				objectProg.getMat4("ModelMatrix").set(modelMatrix);
				getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
			} torusVao.unbind();
		} objectProg.unbind();

		// TODO bug
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);
		gl.glEnable(GL_STENCIL_TEST);
		gl.glStencilFunc(GL_ALWAYS, 0, ~0);
		gl.glStencilOpSeparate(GL_FRONT, GL_KEEP, GL_KEEP, GL_INCR);
		gl.glStencilOpSeparate(GL_BACK, GL_KEEP, GL_KEEP, GL_DECR);

		shadowProg.bind(); {
			shadowProg.getMat4("CameraMatrix").set(cameraMatrix);
			shadowProg.getMat4("ModelMatrix").set(modelMatrix);
			torusVao.bind(); {
				gl.glCullFace(GL_BACK);
				getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
				gl.glCullFace(GL_FRONT);
				getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
				gl.glCullFace(GL_BACK);
			} torusVao.unbind();
		} shadowProg.unbind();

		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);
		getDrawHelper().glClearDepth();
		gl.glStencilFunc(GL_EQUAL, 0, ~0);
		gl.glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);

		objectProg.bind(); {
			objectProg.getFloat("LightMult").set(2.5f);
			planeVao.bind(); {
				objectProg.getMat4("ModelMatrix").set(identity);
				objectProg.getVec3("Color").set(0.8f, 0.7f, 0.4f);
				gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			} planeVao.unbind();
			torusVao.bind(); {
				objectProg.getMat4("ModelMatrix").set(modelMatrix);
				objectProg.getVec3("Color").set(0.9f, 0.8f, 0.1f);
				getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
			} torusVao.unbind();
		} objectProg.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {

		int width = (int) newViewport.width, height = (int) newViewport.height;

		getDrawHelper().glViewPort(width, height);
		perspectiveX(projectionMatrix, fov, newViewport.aspectRatio, 1, 30);
		objectProg.bind();
		objectProg.getMat4("ProjectionMatrix").set(projectionMatrix);
		objectProg.unbind();
		shadowProg.bind();
		shadowProg.getMat4("ProjectionMatrix").set(projectionMatrix);
		shadowProg.unbind();
	}
}
