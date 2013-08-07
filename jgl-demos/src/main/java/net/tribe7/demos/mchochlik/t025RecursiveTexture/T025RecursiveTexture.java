package net.tribe7.demos.mchochlik.t025RecursiveTexture;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Cube;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.glsl.attribute.GLUSampler;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/025_recursive_texture.png")
public class T025RecursiveTexture extends GL3EventListener {

	int currentFbo = 0;
	int texSide = 512;
	int width = texSide, height = texSide;

	private Cube cube = new Cube();
	private GLBuffer cubeVertices;
	private GLProgram p;
	private GLFrameBufferCompact [] fbos = new GLFrameBufferCompact[2];
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix, uModelMatrix;
	private GLUSampler uTexUnit;

	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private ModelTransform cubeTransform = new ModelTransform();
	private Vector3 camTarget = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t025RecursiveTexture/recursiveTexture.vs",
				"/net/tribe7/demos/mchochlik/t025RecursiveTexture/recursiveTexture.fs", gl);

		for (int k = 0; k < fbos.length; k++) {

			GLFrameBufferCompact fb = new GLFrameBufferCompact();
			fb.init(gl);
			fb.setBindTarget(GL_DRAW_FRAMEBUFFER);
			fb.bind(); {
				fb.getColorAttachment().getImage().getMetadata().setWidth(width);
				fb.getColorAttachment().getImage().getMetadata().setHeight(height);
				fb.getColorAttachment().getImage().getMetadata().setInternalFormat(GL_RGBA);
				fb.getColorAttachment().getImage().getMetadata().setPixelDataFormat(GL_RGBA);
				fb.getColorAttachment().getImage().getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);
				fb.getDepthStencilBuffer().getBufferFormat().setWidth(texSide);
				fb.getDepthStencilBuffer().getBufferFormat().setHeight(texSide);
				fb.getDepthStencilBuffer().getBufferFormat().setInternalFormat(GL_DEPTH_COMPONENT);
				fb.getColorAttachment().getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				fb.getColorAttachment().getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				fb.getColorAttachment().getParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
				fb.getColorAttachment().getParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
				fb.initAttachments();
			} fb.unbind();
			fbos[k] = fb;
		}

		cubeVao.init(gl);
		p.bind(); {
			cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			p.getInterface().getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			p.getInterface().getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			p.getInterface().getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
			p.getInterface().getVec3("LightPos").set(4.0f, 4.0f, -8.0f);

			uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
			uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
			uModelMatrix = p.getInterface().getMat4("ModelMatrix");
			uTexUnit = p.getInterface().getSampler("TexUnit");
		}

		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		gl.glCullFace(GL_BACK);
		getDrawHelper().glFrontFace(cube.getFaceWinding());
		gl.glClearDepth(1.0f);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();
		int front = currentFbo;
		int back = (currentFbo + 1) % 2;
		currentFbo = back;

		uTexUnit.set(fbos[front].getColorAttachment());

		orbit(cameraMatrix, camTarget, 3.0, 
				azimuth.setDegrees(time * 35), 
				elevation.setDegrees(sineWave(time / 20.0) * 60));
		uCameraMatrix.set(cameraMatrix);

		cubeTransform.getRotationX().setFullCircles(time * 0.25);
		uModelMatrix.set(cubeTransform.getModelMatrix());

		perspectiveX(projMatrix, fov.setDegrees(40), 1.0, 1, 40);
		uProjectionMatrix.set(projMatrix);

		fbos[front].getColorAttachment().bind(); {

			fbos[back].bind(); {
				getDrawHelper().glViewPort(texSide, texSide);
				getDrawHelper().glClearColor().glClearDepth();
				cubeVao.bind();
				getDrawHelper().glFrontFace(cube.getFaceWinding());
				gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
				cubeVao.unbind();
			} fbos[back].unbind();

			getDrawHelper().glViewPort(width, height);
			getDrawHelper().glClearColor().glClearDepth();
			time += 0.3;

			orbit(cameraMatrix, camTarget, 3.0, 
					azimuth.setDegrees(time * 35), 
					elevation.setDegrees(sineWave(time / 20.0) * 60));
			uCameraMatrix.set(cameraMatrix);

			perspectiveX(projMatrix, fov.setDegrees(75), (double) width / height, 1, 40);
			uProjectionMatrix.set(projMatrix);

			cubeVao.bind();
			getDrawHelper().glFrontFace(cube.getFaceWinding());
			gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
			cubeVao.unbind();
		} fbos[front].getColorAttachment().unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		width = (int) newViewport.width;
		height = (int) newViewport.height;
	}
}
