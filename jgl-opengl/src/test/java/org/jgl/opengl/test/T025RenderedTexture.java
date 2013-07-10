package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.*;
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.*;
import org.jgl.opengl.util.*;
import org.jgl.time.util.ExecutionState;

public class T025RenderedTexture extends GL3EventListener {

	int texSide = 512;
	int width = texSide, height = texSide;
	private Cube cube = new Cube();
	private Torus torus = new Torus(1.0, 0.5, 72, 48);
	private GLProgram cubeProgram;
	private GLProgram torusProgram;
	private GLFrameBuffer fbo = new GLFrameBuffer();
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

		GLShader vertexShader = loadVertexShader("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025RenderedTexture/renderedTexture.vs");
		GLShader cubeFragmentShader = loadFragmentShader("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025RenderedTexture/cube.fs");
		GLShader torusFragmentShader = loadFragmentShader("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025RenderedTexture/torus.fs");

		cubeProgram = new GLProgram().attachShader(vertexShader).attachShader(cubeFragmentShader);
		torusProgram = new GLProgram().attachShader(vertexShader).attachShader(torusFragmentShader);

		fbo.init(gl);
		fbo.setBindTarget(GL_DRAW_FRAMEBUFFER);
		fbo.bind(); {
			fbo.setColorAttachment(0);
			fbo.getColorAttachmentFormat().setWidth(width);
			fbo.getColorAttachmentFormat().setHeight(height);
			fbo.getColorAttachmentFormat().setInternalFormat(GL_RGBA);
			fbo.getColorAttachmentFormat().setPixelDataFormat(GL_RGBA);
			fbo.getColorAttachmentFormat().setPixelDataType(GL_UNSIGNED_BYTE);
			fbo.getDepthStencilBuffer().getBufferFormat().setWidth(texSide);
			fbo.getDepthStencilBuffer().getBufferFormat().setHeight(texSide);
			fbo.getDepthStencilBuffer().getBufferFormat().setInternalFormat(GL_DEPTH_COMPONENT);
			fbo.getColorAttachmentParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			fbo.getColorAttachmentParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			fbo.getColorAttachmentParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
			fbo.getColorAttachmentParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
			fbo.initAttachments();
		} fbo.unbind();

		cubeProgram.init(gl);
		cubeVao.init(gl);
		torusProgram.init(gl);
		torusVao.init(gl);

		cubeProgram.bind(); {
			cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			cubeProgram.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			cubeProgram.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			cubeProgram.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
			cubeProgram.getVec3("LightPos").set(4.0f, 4.0f, -8.0f);
			cubeProgram.getSampler2D("TexUnit").set(fbo.getColorAttachment(0));
			fbo.getColorAttachment(0).bind();
		} cubeProgram.unbind();

		torusProgram.bind(); {
			GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer torusTexCoords = buffer(torus.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
			torusProgram.getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();
			torusProgram.getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();
			torusProgram.getStageAttribute("TexCoord").set(torusVao, torusTexCoords, false, 0).enable();
			torusProgram.getVec3("LightPos").set(2.0f, 3.0f, 4.0f);
			perspectiveX(projMatrix, fov.setDegrees(60), 1.0, 1, 30);
			torusProgram.getMat4("ProjectionMatrix").set(projMatrix);
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
			torusProgram.getMat4("CameraMatrix").set(cameraMatrix);
			double rotation = time * 0.25;
			torusTransform.getRotationX().setFullCircles(rotation);
			torusTransform.getRotationY().setFullCircles(rotation);
			torusTransform.getRotationZ().setFullCircles(rotation);
			torusProgram.getMat4("ModelMatrix").set(torusTransform.getModelMatrix());
		} torusProgram.unbind();

		cubeProgram.bind(); {
			perspectiveX(projMatrix, fov.setDegrees(70), 
					(double) width / height, 1, 30);
			cubeProgram.getMat4("ProjectionMatrix").set(projMatrix);
			orbit(cameraMatrix, camTarget, 3.0, 
					azimuth.setDegrees(time * 35), 
					elevation.setDegrees(sineWave(time / 20.0) * 60));
			cubeProgram.getMat4("CameraMatrix").set(cameraMatrix);
			cubeTransform.getRotationX().setFullCircles(time * 0.25);
			cubeProgram.getMat4("ModelMatrix").set(cubeTransform.getModelMatrix());
		} cubeProgram.unbind();
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		width = (int) newViewport.width;
		height = (int) newViewport.height;
	}
}
