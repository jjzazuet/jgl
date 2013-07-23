package net.tribe7.demos.mchochlik.t022ParallaxMapping;

import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.math.vector.VectorOps.*;
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
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.opengl.util.SphereBumpMap;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/022_parallax_map.png")
public class T022ParallaxMapping extends GL3EventListener {

	private Cube cube = new Cube();
	
	private GLProgram p;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVertices;
	private GLTexture2D bumpTexture = new GLTexture2D();

	private Angle fov = new Angle();
	private Angle elevation = new Angle();
	private Angle azimuth = new Angle();
	private Angle lightAzimuth = new Angle();
	
	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	private GLUFloatVec3 uLightPos;
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private ModelTransform modelTransform = new ModelTransform();
	private Vector3 camTarget = new Vector3();
	private Vector3 lightPos = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t022ParallaxMapping/parallaxMapping.vs", 
				"/net/tribe7/demos/mchochlik/t022ParallaxMapping/parallaxMapping.fs", gl);

		p.bind();
		cubeVao.init(gl);

		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeTangents = buffer(cube.getTangents(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
		p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
		p.getStageAttribute("Tangent").set(cubeVao, cubeTangents, false, 0).enable();
		p.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();

		uLightPos = p.getVec3("LightPos");
		uCameraMatrix = p.getMat4("CameraMatrix");
		uModelMatrix = p.getMat4("ModelMatrix");
		uProjectionMatrix = p.getMat4("ProjectionMatrix");

		SphereBumpMap bumpMap = new SphereBumpMap(512, 512, 2, 2);

		bumpTexture.init(gl);
		bumpTexture.bind(); {
			bumpTexture.getImage().setFrom(bumpMap);
			bumpTexture.loadData();
			bumpTexture.generateMipMap();
			bumpTexture.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			bumpTexture.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			bumpTexture.getParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
			bumpTexture.getParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
			bumpTexture.applyParameters();
		} bumpTexture.unbind();

		p.getSampler("BumpTex").set(bumpTexture);
		p.getInt("BumpTexWidth").set(bumpTexture.getImage().getMetadata().getWidth());
		p.getInt("BumpTexHeight").set(bumpTexture.getImage().getMetadata().getHeight());

		gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		getDrawHelper().glFrontFace(cube.getFaceWinding());
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		getDrawHelper().glClearColor().glClearDepth();
		cubeVao.bind();
		bumpTexture.bind();
		gl.glCullFace(GL_BACK);
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
		bumpTexture.unbind();
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		lightAzimuth.setFullCircles(time * -0.5);
		lightPos.set(-lightAzimuth.cos(), 1, -lightAzimuth.sin());
		scale(lightPos, 2.0);
		uLightPos.set(lightPos);

		orbit(cameraMatrix, camTarget, 3.0, 
				azimuth.setDegrees(-45), 
				elevation.setDegrees(sineWave(time / 30.0) * 70));
		uCameraMatrix.set(cameraMatrix);

		double circle = -time * 0.025;
		modelTransform.getRotationX().setFullCircles(circle);
		modelTransform.getRotationY().setFullCircles(circle);
		modelTransform.getRotationZ().setFullCircles(circle);
		uModelMatrix.set(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMatrix, fov.setDegrees(54), newViewport.aspectRatio, 1, 10);
		uProjectionMatrix.set(projMatrix);
	}
}
