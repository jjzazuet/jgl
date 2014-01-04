package net.tribe7.demos.mchochlik.t016MetallicTorus;

import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL.*;

import javax.media.opengl.GL3;
import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Torus;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.opengl.*;
import net.tribe7.opengl.camera.GLPointCamera;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/016_metallic_torus.png")
public class T016MetallicTorus extends GL3EventListener {

	private GLProgram p;
	private GLBuffer torusIndices;

	private Torus torus = new Torus(1.0, 0.5, 72, 48);
	private ModelTransform torusTransform = new ModelTransform();
	private GLPointCamera camera = new GLPointCamera(80);

	private GLVertexArray torusVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix, uModelMatrix;
	private GLUInt uColorCount;
	private GLUFloatVec4 uColor;

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t016MetallicTorus/metallicTorus.vs", 
				"/net/tribe7/demos/mchochlik/t016MetallicTorus/metallicTorus.fs", gl);

		torusVao.init(gl);
		p.bind();

		GLBuffer torusPositions = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("Position").set(torusVao, torusPositions, false, 0).enable();

		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();

		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		uModelMatrix = p.getInterface().getMat4("ModelMatrix");
		uColorCount = p.getInterface().getInt("ColorCount");
		uColor = p.getInterface().getVec4("Color");

		uColorCount.set(8);
		uColor.set(0, new float [] {1.0f, 1.0f, 0.9f, 1.00f});
		uColor.set(1, new float [] {1.0f, 0.9f, 0.8f, 0.97f});
		uColor.set(2, new float [] {0.9f, 0.7f, 0.5f, 0.95f});
		uColor.set(3, new float [] {0.5f, 0.5f, 1.0f, 0.95f});
		uColor.set(4, new float [] {0.2f, 0.2f, 0.7f, 0.00f});
		uColor.set(5, new float [] {0.1f, 0.1f, 0.1f, 0.00f});
		uColor.set(6, new float [] {0.2f, 0.2f, 0.2f,-0.10f});
		uColor.set(7, new float [] {0.5f, 0.5f, 0.5f,-1.00f});

		gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
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
		orbit(camera.getMatrix(), camera.getTarget(),
				3.5, 
				camera.getAzimuth().setDegrees(time * 35), 
				camera.getElevation().setDegrees(sineWave(time / 60) * 80));
		uCameraMatrix.set(camera.getMatrix());
		torusTransform.getRotationX().setFullCircles(time * 0.25);
		uModelMatrix.set(torusTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(camera.getProjection(), camera.getFov(), 
				newViewport.width/newViewport.height, 1, 20);
		uProjectionMatrix.set(camera.getProjection());
	}
}