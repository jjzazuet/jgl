package net.tribe7.demos.mchochlik.t027DepthOfField;

import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.angle.AngleOps.*;

import java.util.List;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Cube;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/027_depth_of_field.png")
public class T027DepthOfField extends GL3EventListener {

	private int sampleQuality = 128;
	private Cube cube = new Cube();
	private List<BufferedMatrix4> cubeMatrices = new MakeCubeMatrices().apply(100, 10.0f);

	private Vector3 cameraTarget = new Vector3(), ambientColor = new Vector3(), diffuseColor = new Vector3();
	private Angle fov = new Angle(), azimuth = new Angle(), elevation = new Angle();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();

	private GLProgram mainProg, dofProg;
	private GLTextureFrameBuffer fbo = new GLTextureFrameBuffer();
	private GLVertexArray cubeVao = new GLVertexArray(), screenVao = new GLVertexArray();

	private final float [] screenVerts = new float [] { 
			-1.0f, -1.0f,
			-1.0f,  1.0f,
			 1.0f, -1.0f,
			 1.0f,  1.0f
	};

	@Override
	protected void doInit(GL3 gl) throws Exception {

		mainProg = loadProgram("/net/tribe7/demos/mchochlik/t027DepthOfField/main.vs", 
				"/net/tribe7/demos/mchochlik/t027DepthOfField/main.fs", gl);
		dofProg = loadProgram("/net/tribe7/demos/mchochlik/t027DepthOfField/dof.vs", 
				"/net/tribe7/demos/mchochlik/t027DepthOfField/dof.fs", gl);

		initResource(gl, fbo, cubeVao, screenVao);

		GLBuffer cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		mainProg.bind(); {
			mainProg.getInterface().getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			mainProg.getInterface().getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			mainProg.getInterface().getVec3("LightPos").set(30.0, 50.0, 20.0);
		} mainProg.unbind();

		GLBuffer screenVertices = buffer(screenVerts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2);

		fbo.setBindTarget(GL_DRAW_FRAMEBUFFER);
		fbo.bind();
		fbo.initAttachments();
		fbo.unbind();

		dofProg.bind(); {
			dofProg.getInterface().getStageAttribute("Position").set(screenVao, screenVertices, false, 0).enable();
			dofProg.getInterface().getInt("SampleMult").set(sampleQuality);
			dofProg.getInterface().getSampler("ColorTex").set(fbo.getColorAttachment());
			fbo.getColorAttachment().bind();
			dofProg.getInterface().getSampler("DepthTex").set(fbo.getDepthAttachment());
			fbo.getDepthAttachment().bind();
		} dofProg.unbind();

		gl.glClearColor(0.9f, 0.9f, 0.9f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glEnable(GL_LINE_SMOOTH);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		fbo.bind(); {
			getDrawHelper().glClearColor().glClearDepth();
			mainProg.bind(); {
				cubeVao.bind(); {
					for (BufferedMatrix4 m : cubeMatrices) {

						mainProg.getInterface().getMat4("ModelMatrix").set(m);

						ambientColor.set(0.7f, 0.6f, 0.2f);
						diffuseColor.set(1.0f, 0.8f, 0.3f);
						mainProg.getInterface().getVec3("AmbientColor").set(ambientColor);
						mainProg.getInterface().getVec3("DiffuseColor").set(diffuseColor);
						gl.glDrawArrays(GL_TRIANGLES, 0, 36);

						ambientColor.set(0.7f, 0.6f, 0.2f);
						diffuseColor.set(1.0f, 0.8f, 0.3f);
						mainProg.getInterface().getVec3("AmbientColor").set(ambientColor);
						mainProg.getInterface().getVec3("DiffuseColor").set(diffuseColor);
						gl.glDrawArrays(GL_LINE_LOOP, 0, 36);
					}
				} cubeVao.unbind();
			} mainProg.unbind();
		} fbo.unbind();

		getDrawHelper().glClearColor().glClearDepth();

		dofProg.bind(); {
			screenVao.bind(); {
				gl.glEnable(GL_BLEND);
				gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
				gl.glDisable(GL_BLEND);
			} screenVao.unbind();
		} dofProg.unbind();	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		orbit(cameraMatrix, cameraTarget, 20.5, 
				azimuth.setFullCircles(time / 20.0), 
				elevation.setDegrees(sineWave(time / 25.0) * 30));

		mainProg.bind(); {
			mainProg.getInterface().getMat4("CameraMatrix").set(cameraMatrix);
		} mainProg.unbind();

		dofProg.bind(); {
			dofProg.getInterface().getFloat("FocusDepth").set((float) (0.6 + sineWave(time / 9.0) * .3));
		} dofProg.unbind();
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {

		int width = (int) newViewport.width;
		int height = (int) newViewport.height;
		getDrawHelper().glViewPort(width, height);
		perspectiveX(projMatrix, fov.setDegrees(65), newViewport.aspectRatio, 4, 50);

		dofProg.bind(); {
			dofProg.getInterface().getInt("ViewportWidth").set(width);
			dofProg.getInterface().getInt("ViewportHeight").set(height);
		} dofProg.unbind();

		mainProg.bind(); {
			mainProg.getInterface().getMat4("ProjectionMatrix").set(projMatrix);
		} mainProg.unbind();
	}
}
