package net.tribe7.demos.mchochlik.t025RenderedTexture;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.*;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.util.*;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/025_rendered_texture.png")
public class T025RenderedTexture extends GL3EventListener {

	int texSide = 512;
	int width = texSide, height = texSide;
	private Cube cube = new Cube();
	private Torus torus = new Torus(1.0, 0.5, 72, 48);
	private GLProgram cubeProgram;
	private GLProgram torusProgram;
	private GLFrameBufferCompact fbo = new GLFrameBufferCompact();
	private GLBuffer torusIndices;
	private GLBuffer cubeVertices;

	private GLVertexArray cubeVao = new GLVertexArray();
	private GLVertexArray torusVao = new GLVertexArray();

	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private ModelTransform torusTransform = new ModelTransform();
	private ModelTransform cubeTransform = new ModelTransform();
	private Vector3 camTarget = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		GLShader vertexShader = loadVertexShader("/net/tribe7/demos/mchochlik/t025RenderedTexture/renderedTexture.vs");
		GLShader cubeFragmentShader = loadFragmentShader("/net/tribe7/demos/mchochlik/t025RenderedTexture/cube.fs");
		GLShader torusFragmentShader = loadFragmentShader("/net/tribe7/demos/mchochlik/t025RenderedTexture/torus.fs");

		cubeProgram = new GLProgram().attachShader(vertexShader).attachShader(cubeFragmentShader);
		torusProgram = new GLProgram().attachShader(vertexShader).attachShader(torusFragmentShader);

		fbo.init(gl);
		fbo.setBindTarget(GL_DRAW_FRAMEBUFFER);
		fbo.bind(); {
			fbo.getColorAttachment().getImage().getMetadata().setWidth(width);
			fbo.getColorAttachment().getImage().getMetadata().setHeight(height);
			fbo.getColorAttachment().getImage().getMetadata().setInternalFormat(GL_RGBA);
			fbo.getColorAttachment().getImage().getMetadata().setPixelDataFormat(GL_RGBA);
			fbo.getColorAttachment().getImage().getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);
			fbo.getDepthStencilBuffer().getBufferFormat().setWidth(texSide);
			fbo.getDepthStencilBuffer().getBufferFormat().setHeight(texSide);
			fbo.getDepthStencilBuffer().getBufferFormat().setInternalFormat(GL_DEPTH_COMPONENT);
			fbo.getColorAttachment().getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			fbo.getColorAttachment().getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			fbo.getColorAttachment().getParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
			fbo.getColorAttachment().getParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
			fbo.initAttachments();
		} fbo.unbind();

		initResource(gl, cubeProgram, cubeVao, torusProgram, torusVao);

		cubeProgram.bind(); {
			cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			cubeProgram.getInterface().getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			cubeProgram.getInterface().getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			cubeProgram.getInterface().getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
			cubeProgram.getInterface().getVec3("LightPos").set(4.0f, 4.0f, -8.0f);
			cubeProgram.getInterface().getSampler("TexUnit").set(fbo.getColorAttachment());
			fbo.getColorAttachment().bind();
		} cubeProgram.unbind();

		torusProgram.bind(); {
			GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer torusTexCoords = buffer(torus.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
			torusProgram.getInterface().getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();
			torusProgram.getInterface().getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();
			torusProgram.getInterface().getStageAttribute("TexCoord").set(torusVao, torusTexCoords, false, 0).enable();
			torusProgram.getInterface().getVec3("LightPos").set(2.0f, 3.0f, 4.0f);
			perspectiveX(projMatrix, fov.setDegrees(60), 1.0, 1, 30);
			torusProgram.getInterface().getMat4("ProjectionMatrix").set(projMatrix);
		}
		torusProgram.unbind();

		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		fbo.bind();
		torusProgram.bind(); {
			getDrawHelper().glViewPort(texSide, texSide);
			gl.glClearDepth(1.0f);
			gl.glClearColor(0.4f, 0.9f, 0.4f, 1.0f);
			getDrawHelper().glClearColor().glClearDepth();
			torusVao.bind();
			getDrawHelper().glFrontFace(torus.getFaceWinding());
			getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
			torusVao.unbind();
		} torusProgram.unbind();
		fbo.unbind();

		cubeProgram.bind(); {
			getDrawHelper().glViewPort(width, height);
			gl.glClearDepth(1.0f);
			gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
			getDrawHelper().glClearColor().glClearDepth();
			cubeVao.bind();
			getDrawHelper().glFrontFace(cube.getFaceWinding());
			gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
			cubeVao.unbind();
		} cubeProgram.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		torusProgram.bind(); {
			orbit(cameraMatrix, camTarget, 3.5, 
					azimuth.setDegrees(time * 25), 
					elevation.setDegrees(sineWave(time / 30.0) * 90));
			torusProgram.getInterface().getMat4("CameraMatrix").set(cameraMatrix);
			double rotation = time * 0.25;
			torusTransform.getRotationX().setFullCircles(rotation);
			torusTransform.getRotationY().setFullCircles(rotation);
			torusTransform.getRotationZ().setFullCircles(rotation);
			torusProgram.getInterface().getMat4("ModelMatrix").set(torusTransform.getModelMatrix());
		} torusProgram.unbind();

		cubeProgram.bind(); {
			perspectiveX(projMatrix, fov.setDegrees(70), 
					(double) width / height, 1, 30);
			cubeProgram.getInterface().getMat4("ProjectionMatrix").set(projMatrix);
			orbit(cameraMatrix, camTarget, 3.0, 
					azimuth.setDegrees(time * 35), 
					elevation.setDegrees(sineWave(time / 20.0) * 60));
			cubeProgram.getInterface().getMat4("CameraMatrix").set(cameraMatrix);
			cubeTransform.getRotationX().setFullCircles(time * 0.25);
			cubeProgram.getInterface().getMat4("ModelMatrix").set(cubeTransform.getModelMatrix());
		} cubeProgram.unbind();
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		width = (int) newViewport.width;
		height = (int) newViewport.height;
	}
}
