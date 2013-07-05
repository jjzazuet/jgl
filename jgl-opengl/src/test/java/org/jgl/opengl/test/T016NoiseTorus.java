package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;

import java.nio.ByteBuffer;
import java.util.Random;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Torus;
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.GL3EventListener;
import org.jgl.opengl.GLBuffer;
import org.jgl.opengl.GLTexture2D;
import org.jgl.opengl.GLTexture2DImage;
import org.jgl.opengl.GLVertexArray;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUFloatMat4;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

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

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016NoiseTorus/noiseTorus.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016NoiseTorus/noiseTorus.fs", gl);

		p.bind();
		torusVao.init(gl);

		GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();

		GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();

		GLBuffer torusTexCoords = buffer(torus.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.getStageAttribute("TexCoord").set(torusVao, torusTexCoords, false, 0).enable();

		uCameraMatrix = p.getMat4("CameraMatrix");
		uModelMatrix = p.getMat4("ModelMatrix");
		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		torusIndices = buffer(torus.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);

		noiseTexture.init(gl); {

			noiseTexture.setTextureTarget(GL_TEXTURE_2D);
			noiseTexture.bind();
			int s = 256;
			byte [] texData = new byte[s*s];

			for(int v=0;v!=s;++v) {
				for(int u=0;u!=s;++u) {
					texData[v*s+u] = (byte) (r.nextInt() % 0x100);	
				}
			}

			GLTexture2DImage noiseImage = new GLTexture2DImage();

			noiseImage.setImageData(ByteBuffer.wrap(texData));
			noiseImage.getMetadata().setWidth(s);
			noiseImage.getMetadata().setHeight(s);
			noiseImage.getMetadata().setInternalFormat(GL_RED);
			noiseImage.getMetadata().setPixelDataFormat(GL_RED);
			noiseImage.getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);

			noiseTexture.loadData(noiseImage);
			noiseTexture.setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			noiseTexture.setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			noiseTexture.setParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
			noiseTexture.setParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
			noiseTexture.setParameter(GL_TEXTURE_SWIZZLE_G, GL_RED);
			noiseTexture.setParameter(GL_TEXTURE_SWIZZLE_B, GL_RED);

			p.getSampler2D("TexUnit").set(noiseTexture);
		}

		p.getVec3("LightPos").set(4.0f, 4.0f, -8.0f);

		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		glFrontFace(gl, torus.getFaceWinding());
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState)
			throws Exception {
		torusVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		gl.glCullFace(GL_FRONT);
		glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());

		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		gl.glCullFace(GL_BACK);
		glIndexedDraw(GL_TRIANGLE_STRIP, torusIndices, torus.getPrimitiveRestartIndex());
		torusVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState)
			throws Exception {

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
		glViewPort(gl, newViewport);
		perspectiveX(projMatrix, fov.setDegrees(60), newViewport.aspectRatio, 1, 20);
		uProjectionMatrix.set(projMatrix);
	}
}
