package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import static javax.media.opengl.GL2GL3.GL_DRAW_FRAMEBUFFER;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Cube;
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.GL3EventListener;
import org.jgl.opengl.GLBuffer;
import org.jgl.opengl.GLFrameBuffer;
import org.jgl.opengl.GLVertexArray;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUFloatMat4;
import org.jgl.opengl.glsl.attribute.GLUSampler2D;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T025RecursiveTexture extends GL3EventListener {

	int currentFbo = 0;
	int texSide = 512;
	int width = texSide, height = texSide;

	private Cube cube = new Cube();
	private GLBuffer cubeVertices;
	private GLProgram p;
	private GLFrameBuffer [] fbos = new GLFrameBuffer[2];
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix, uModelMatrix;
	private GLUSampler2D uTexUnit;

	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private ModelTransform cubeTransform = new ModelTransform();
	private Vector3 camTarget = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025recursiveTexture/recursiveTexture.vs",
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025recursiveTexture/recursiveTexture.fs", gl);

		for (int k = 0; k < fbos.length; k++) {

			GLFrameBuffer fb = new GLFrameBuffer();
			fb.init(gl);
			fb.setBindTarget(GL_DRAW_FRAMEBUFFER);
			fb.bind(); {
				fb.setColorAttachment(0);
				fb.getColorAttachmentFormat().setWidth(width);
				fb.getColorAttachmentFormat().setHeight(height);
				fb.getColorAttachmentFormat().setInternalFormat(GL_RGBA);
				fb.getColorAttachmentFormat().setPixelDataFormat(GL_RGBA);
				fb.getColorAttachmentFormat().setPixelDataType(GL_UNSIGNED_BYTE);
				fb.getDepthStencilBuffer().getBufferFormat().setWidth(texSide);
				fb.getDepthStencilBuffer().getBufferFormat().setHeight(texSide);
				fb.getDepthStencilBuffer().getBufferFormat().setInternalFormat(GL_DEPTH_COMPONENT);
				fb.getColorAttachmentParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				fb.getColorAttachmentParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				fb.getColorAttachmentParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
				fb.getColorAttachmentParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
				fb.initAttachments();
			} fb.unbind();
			fbos[k] = fb;
		}

		cubeVao.init(gl);
		p.bind(); {
			cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			p.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
			p.getVec3("LightPos").set(4.0f, 4.0f, -8.0f);

			uProjectionMatrix = p.getMat4("ProjectionMatrix");
			uCameraMatrix = p.getMat4("CameraMatrix");
			uModelMatrix = p.getMat4("ModelMatrix");
			uTexUnit = p.getSampler2D("TexUnit");
		}

		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		gl.glCullFace(GL_BACK);
		glFrontFace(gl, cube.getFaceWinding());
		gl.glClearDepth(1.0f);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();
		int front = currentFbo;
		int back = (currentFbo + 1) % 2;
		currentFbo = back;

		uTexUnit.set(fbos[front].getColorAttachment(0));
		orbit(cameraMatrix, camTarget, 3.0, 
				azimuth.setDegrees(time * 35), 
				elevation.setDegrees(sineWave(time / 20.0) * 60));
		uCameraMatrix.set(cameraMatrix);

		cubeTransform.getRotationX().setFullCircles(time * 0.25);
		uModelMatrix.set(cubeTransform.getModelMatrix());

		perspectiveX(projMatrix, fov.setDegrees(40), 1.0, 1, 40);
		uProjectionMatrix.set(projMatrix);

		fbos[back].bind(); {
			glViewPort(gl, texSide, texSide);
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			cubeVao.bind();
			glFrontFace(gl, cube.getFaceWinding());
			gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
			cubeVao.unbind();
		} fbos[back].unbind();

		glViewPort(gl, width, height);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		time += 0.3;

		orbit(cameraMatrix, camTarget, 3.0, 
				azimuth.setDegrees(time * 35), 
				elevation.setDegrees(sineWave(time / 20.0) * 60));
		uCameraMatrix.set(cameraMatrix);

		perspectiveX(projMatrix, fov.setDegrees(75), (double) width / height, 1, 40);
		uProjectionMatrix.set(projMatrix);

		cubeVao.bind();
		glFrontFace(gl, cube.getFaceWinding());
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		width = (int) newViewport.width;
		height = (int) newViewport.height;
	}
}
