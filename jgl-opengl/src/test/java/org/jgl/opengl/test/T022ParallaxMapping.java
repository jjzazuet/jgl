package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;
import static org.jgl.math.vector.VectorOps.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Cube;
import org.jgl.geom.transform.ModelTransform;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.*;
import org.jgl.opengl.test.util.SphereBumpMap;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T022ParallaxMapping extends GL3EventListener {

	private Cube cube = new Cube();
	
	private GLProgram p;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVertices;
	
	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();
	private Angle lightazimuth = new Angle();
	
	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	private GLUFloatVec3 uLightPos;
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private ModelTransform modelTransform = new ModelTransform();
	private Vector3 camTarget = new Vector3();
	private Vector3 lightPos = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t022ParallaxMapping/parallaxMapping.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t022ParallaxMapping/parallaxMapping.fs", gl);
		
		p.bind();
		cubeVao.init(gl);
		
		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		GLBuffer cubeTangents = buffer(cube.getTangents(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		GLTexture2D bumpTexture = new GLTexture2D();

		p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
		p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
		p.getStageAttribute("Tangent").set(cubeVao, cubeTangents, false, 0).enable();
		p.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();

		uLightPos = p.getVec3("LightPos");
		uCameraMatrix = p.getMat4("CameraMatrix");
		uModelMatrix = p.getMat4("ModelMatrix");
		uProjectionMatrix = p.getMat4("ProjectionMatrix");

		bumpTexture.init(gl);
		bumpTexture.setTextureTarget(GL_TEXTURE_2D);
		bumpTexture.setTextureUnit(0);
		bumpTexture.bind(); {

			SphereBumpMap bumpMap = new SphereBumpMap(512, 512, 2, 2);
			
			bumpTexture.loadData(bumpMap);
			bumpTexture.generateMipMap();
			bumpTexture.setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			bumpTexture.setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			bumpTexture.setParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
			bumpTexture.setParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);

			p.getSampler2D("BumpTex").set(bumpTexture);
			p.getInt("BumpTexWidth").set(bumpTexture.getImage().getMetadata().getWidth());
			p.getInt("BumpTexHeight").set(bumpTexture.getImage().getMetadata().getHeight());
		}

		gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		glFrontFace(gl, cube.getFaceWinding());
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		cubeVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glCullFace(GL_BACK);
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		
		double time = currentState.getElapsedTimeSeconds();
		
		lightazimuth.setFullCircles(time * -0.5);
		lightPos.set(-lightazimuth.cos(), 1, -lightazimuth.sin());
		scale(lightPos, 2.0);
		uLightPos.set(lightPos);
		
		orbit(cameraMatrix, camTarget, 3.0, 
				azimuth.setDegrees(-45), 
				elevation.setDegrees(sineWave(time / 30.0) * 70));
		uCameraMatrix.setColMaj(cameraMatrix);
		
		double circle = -time * 0.025;
		modelTransform.getRotationX().setFullCircles(circle);
		modelTransform.getRotationY().setFullCircles(circle);
		modelTransform.getRotationZ().setFullCircles(circle);
		uModelMatrix.setColMaj(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		glViewPort(gl, newViewport);
		perspectiveX(projMatrix, fov.setDegrees(54), newViewport.aspectRatio, 1, 10);
		uProjectionMatrix.setColMaj(projMatrix);
	}
}
