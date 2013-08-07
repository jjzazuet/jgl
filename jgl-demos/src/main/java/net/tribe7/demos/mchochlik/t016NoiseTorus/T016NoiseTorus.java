package net.tribe7.demos.mchochlik.t016NoiseTorus;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL3.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import java.nio.ByteBuffer;
import java.util.Random;

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

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/016_noise_torus.png")
public class T016NoiseTorus extends GL3EventListener {

	private Random r = new Random();
	private Torus torus = new Torus(1.0, 0.5, 72, 48);

	private GLProgram p;
	private GLVertexArray torusVao = new GLVertexArray();
	private GLBuffer torusIndices;
	private GLTexture2D noiseTexture = new GLTexture2D();

	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();

	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private ModelTransform modelTransform = new ModelTransform();
	private Vector3 camTarget = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t016NoiseTorus/noiseTorus.vs", 
				"/net/tribe7/demos/mchochlik/t016NoiseTorus/noiseTorus.fs", gl);

		p.bind();
		torusVao.init(gl);

		GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();

		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();

		GLBuffer torusTexCoords = buffer(torus.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getInterface().getStageAttribute("TexCoord").set(torusVao, torusTexCoords, false, 0).enable();

		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		uModelMatrix = p.getInterface().getMat4("ModelMatrix");
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);

		int s = 256;
		byte [] texData = new byte[s*s];

		for(int v=0;v!=s;++v) {
			for(int u=0;u!=s;++u) {
				texData[v*s+u] = (byte) (r.nextInt() % 0x100);	
			}
		}

		noiseTexture.init(gl);
		noiseTexture.getImage().setImageData(ByteBuffer.wrap(texData));
		noiseTexture.getImage().getMetadata().setWidth(s);
		noiseTexture.getImage().getMetadata().setHeight(s);
		noiseTexture.getImage().getMetadata().setInternalFormat(GL_RED);
		noiseTexture.getImage().getMetadata().setPixelDataFormat(GL_RED);
		noiseTexture.getImage().getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);
		noiseTexture.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		noiseTexture.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		noiseTexture.getParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
		noiseTexture.getParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
		noiseTexture.getParameters().put(GL_TEXTURE_SWIZZLE_G, GL_RED);
		noiseTexture.getParameters().put(GL_TEXTURE_SWIZZLE_B, GL_RED);

		noiseTexture.bind(); {
			noiseTexture.loadData();
			noiseTexture.applyParameters();
		} noiseTexture.unbind();

		p.getInterface().getSampler("TexUnit").set(noiseTexture);
		p.getInterface().getVec3("LightPos").set(4.0f, 4.0f, -8.0f);
		noiseTexture.bind();

		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		getDrawHelper().glFrontFace(torus.getFaceWinding());
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState)
			throws Exception {
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
		orbit(cameraMatrix, camTarget, 4.5,
				azimuth.setDegrees(time * 35), 
				elevation.setDegrees(sineWave(time / 20.0) * 60));
		uCameraMatrix.set(cameraMatrix);
		modelTransform.getRotationX().setFullCircles(time * 0.25);
		uModelMatrix.set(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMatrix, fov.setDegrees(60), newViewport.aspectRatio, 1, 20);
		uProjectionMatrix.set(projMatrix);
	}
}
