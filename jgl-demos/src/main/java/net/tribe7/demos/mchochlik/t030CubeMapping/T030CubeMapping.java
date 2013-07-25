package net.tribe7.demos.mchochlik.t030CubeMapping;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.angle.AngleOps.*;

import java.util.List;
import javax.media.opengl.GL3;
import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.bezier.BezierCubicLoop;
import net.tribe7.geom.solid.*;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.*;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/030_cube_mapping.png")
public class T030CubeMapping extends GL3EventListener {

	private int texSide = 128, width, height;
	private Sphere sphere = new Sphere(1.0, 72, 48);
	private Cube cube = new Cube();
	private Vector4 lightPos = new Vector4();
	private List<Vector3> cubeOffsets = new MakeCubeOffsets().apply(2.5, 6);
	private BezierCubicLoop lightPath = new BezierCubicLoop(
			new Vector3( 0.0f,  6.0f,  0.0f),
			new Vector3(-3.0f, -4.0f,  3.5f),
			new Vector3( 0.0f, -3.0f, -4.0f),
			new Vector3( 3.5f, -4.0f,  3.0f));

	private GLProgram sphereProg, cubeProg = new GLProgram(), cubeMapProg = new GLProgram();
	private GLVertexArray sphereVao = new GLVertexArray();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer sphereIndices;
	private CubeMapFramebuffer fbo = new CubeMapFramebuffer(texSide, texSide);

	private GLUFloatVec4 cmLightPos;
	private GLUFloatVec3 cmOffset;

	private GLUFloatMat4 cubeProjectionMatrix, cubeCameraMatrix;
	private GLUFloatVec4 cubeLightPos;
	private GLUFloatVec3 cubeOffset;

	private GLUFloatMat4 sphereProjectionMatrix, sphereCameraMatrix;
	private GLUFloatVec4 sphereLightPos;
	private GLUFloat sphereTime;

	private BufferedMatrix4 camMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private Angle fov = new Angle(), azimuth = new Angle(), elevation = new Angle();
	private Vector3 cameraTarget = new Vector3();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		sphereProg = loadProgram("/net/tribe7/demos/mchochlik/t030CubeMapping/sphere.vs", 
				"/net/tribe7/demos/mchochlik/t030CubeMapping/sphere.fs", gl);

		GLShader cubeVs = loadVertexShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cube.vs");
		GLShader cubeFs = loadFragmentShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cube.fs");
		GLShader cubeMapVs = loadVertexShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cubeMap.vs");
		GLShader cubeMapGs = loadGeometryShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cubeMap.gs");

		cubeProg.attachShader(cubeVs).attachShader(cubeFs).init(gl);
		cubeMapProg.attachShader(cubeMapVs).attachShader(cubeMapGs).attachShader(cubeFs).init(gl);

		initResource(gl, sphereVao, cubeVao, fbo);

		fbo.setBindTarget(GL_DRAW_FRAMEBUFFER);
		fbo.bind(); {
			fbo.initAttachments();
		} fbo.unbind();

		sphereProg.bind(); {
			GLBuffer sphereVertices = buffer(sphere.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer sphereNormals = buffer(sphere.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer sphereTangents = buffer(sphere.getTangents(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer sphereTexCoords = buffer(sphere.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			sphereIndices = buffer(sphere.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
			sphereProjectionMatrix = sphereProg.getMat4("ProjectionMatrix");
			sphereCameraMatrix = sphereProg.getMat4("CameraMatrix");
			sphereLightPos = sphereProg.getVec4("LightPos");
			sphereTime = sphereProg.getFloat("Time");

			sphereProg.getStageAttribute("Position").set(sphereVao, sphereVertices, false, 0).enable();
			sphereProg.getStageAttribute("Normal").set(sphereVao, sphereNormals, false, 0).enable();
			sphereProg.getStageAttribute("Tangent").set(sphereVao, sphereTangents, false, 0).enable();
			sphereProg.getStageAttribute("TexCoord").set(sphereVao, sphereTexCoords, false, 0).enable();
			sphereProg.getSampler("CubeTex").set(fbo.getCubeColorAttachment());
			fbo.getCubeColorAttachment().bind();
		} sphereProg.unbind();

		GLBuffer cubeVerticesGl = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		cubeMapProg.bind(); {
			perspectiveX(projMatrix, fov.setDegrees(90), 1.0, 1, 20);
			cubeMapProg.getStageAttribute("Position").set(cubeVao, cubeVerticesGl, false, 0).enable();
			cubeMapProg.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			cubeMapProg.getMat4("ProjectionMatrix").set(projMatrix);
			cmLightPos = cubeMapProg.getVec4("LightPos");
			cmOffset = cubeMapProg.getVec3("Offset");
		} cubeMapProg.unbind();

		cubeProg.bind(); {
			cubeProg.getStageAttribute("Position").set(cubeVao, cubeVerticesGl, false, 0).enable();
			cubeProg.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			cubeProjectionMatrix = cubeProg.getMat4("ProjectionMatrix");
			cubeCameraMatrix = cubeProg.getMat4("CameraMatrix");
			cubeLightPos = cubeProg.getVec4("LightPos");
			cubeOffset = cubeProg.getVec3("Offset");
		} cubeProg.unbind();

		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		getDrawHelper().glFrontFace(cube.getFaceWinding());

		fbo.bind(); {
			getDrawHelper().glViewPort(texSide, texSide);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClearDepth(1.0f);
			getDrawHelper().glClearColor().glClearDepth();

			cubeMapProg.bind();
			cubeVao.bind();
			for (Vector3 offset : cubeOffsets) {
				cmOffset.set(offset);
				gl.glDrawArrays(GL_TRIANGLES, 0, 36);
			}
			cubeVao.unbind();
			cubeMapProg.unbind();
		} fbo.unbind();

		getDrawHelper().glViewPort(width, height);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		getDrawHelper().glClearColor().glClearDepth();

		cubeProg.bind(); {
			cubeVao.bind(); {
				for (Vector3 offset : cubeOffsets) {
					cubeOffset.set(offset);
					gl.glDrawArrays(GL_TRIANGLES, 0, 36);
				}
			} cubeVao.unbind();
		} cubeProg.unbind();

		sphereProg.bind(); {
			getDrawHelper().glFrontFace(sphere.getFaceWinding());
			sphereVao.bind(); {
				getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, sphereIndices, sphere.getPrimitiveRestartIndex());
			} sphereVao.unbind();
		} sphereProg.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		lightPos.set(lightPath.pointAt(time/10.0));
		lightPos.setW(1.0);
		perspectiveX(projMatrix, fov.setDegrees(70), (double) width/height, 1, 20);
		orbit(camMatrix, cameraTarget, 4.0, 
				azimuth.setFullCircles(time / 16.0),
				elevation.setDegrees(sineWave(time / 20.0) * 30));

		cubeMapProg.bind(); {
			cmLightPos.set(lightPos);
		} cubeMapProg.unbind();

		cubeProg.bind(); {
			cubeProjectionMatrix.set(projMatrix);
			cubeCameraMatrix.set(camMatrix);
			cubeLightPos.set(lightPos);
		} cubeProg.unbind();

		sphereProg.bind(); {
			sphereProjectionMatrix.set(projMatrix);
			sphereCameraMatrix.set(camMatrix);
			sphereLightPos.set(lightPos);
			sphereTime.set((float) time);
		} sphereProg.unbind();
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		width = (int) newViewport.width;
		height = (int) newViewport.height;
	}
}
