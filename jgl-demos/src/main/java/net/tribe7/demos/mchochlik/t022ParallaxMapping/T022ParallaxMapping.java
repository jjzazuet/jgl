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
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.camera.GLPointCamera;
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

	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	private GLUFloatVec3 uLightPos;
	private Angle lightAzimuth = new Angle();
	private Vector3 lightPos = new Vector3();
	private ModelTransform modelTransform = new ModelTransform();
	private GLPointCamera camera = new GLPointCamera(54);

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

		p.getInterface().getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
		p.getInterface().getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
		p.getInterface().getStageAttribute("Tangent").set(cubeVao, cubeTangents, false, 0).enable();
		p.getInterface().getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();

		uLightPos = p.getInterface().getVec3("LightPos");
		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		uModelMatrix = p.getInterface().getMat4("ModelMatrix");
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");

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

		p.getInterface().getSampler("BumpTex").set(bumpTexture);
		p.getInterface().getInt("BumpTexWidth").set(bumpTexture.getImage().getMetadata().getWidth());
		p.getInterface().getInt("BumpTexHeight").set(bumpTexture.getImage().getMetadata().getHeight());

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

		orbit(camera.getMatrix(), camera.getTarget(), 3.0, 
				camera.getAzimuth().setDegrees(-45), 
				camera.getElevation().setDegrees(sineWave(time / 30.0) * 70));
		uCameraMatrix.set(camera.getMatrix());

		double circle = -time * 0.025;
		modelTransform.getRotationX().setFullCircles(circle);
		modelTransform.getRotationY().setFullCircles(circle);
		modelTransform.getRotationZ().setFullCircles(circle);
		uModelMatrix.set(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(camera.getProjection(), camera.getFov(), newViewport.aspectRatio, 1, 10);
		uProjectionMatrix.set(camera.getProjection());
	}
}
