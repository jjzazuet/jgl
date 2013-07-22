package net.tribe7.demos.mchochlik.t030CubeMapping;

import static javax.media.opengl.GL.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.*;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/030_cube_mapping.png")
public class T030CubeMapping extends GL3EventListener {

	private Sphere sphere = new Sphere(1.0, 72, 48);
	private Cube cube = new Cube();	

	private GLProgram sphereProg, cubeProg = new GLProgram(), cubeMapProg = new GLProgram();
	private GLVertexArray sphereVao = new GLVertexArray();
	private GLVertexArray cubeVao = new GLVertexArray();

	//private BufferedMatrix4 camMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private Angle fov = new Angle();

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

		initResource(gl, sphereVao, cubeVao);

		sphereProg.bind(); {
			GLBuffer sphereVertices = buffer(sphere.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer sphereNormals = buffer(sphere.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer sphereTangents = buffer(sphere.getTangents(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer sphereTexCoords = buffer(sphere.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			sphereProg.getStageAttribute("Position").set(sphereVao, sphereVertices, false, 0).enable();
			sphereProg.getStageAttribute("Normal").set(sphereVao, sphereNormals, false, 0).enable();
			sphereProg.getStageAttribute("Tangent").set(sphereVao, sphereTangents, false, 0).enable();
			sphereProg.getStageAttribute("TexCoord").set(sphereVao, sphereTexCoords, false, 0).enable();
		} sphereProg.unbind();

		GLBuffer cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		cubeMapProg.bind(); {
			perspectiveX(projMatrix, fov.setDegrees(90), 1.0, 1, 20);
			cubeMapProg.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			cubeMapProg.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			cubeMapProg.getMat4("ProjectionMatrix").set(projMatrix);
		} cubeMapProg.unbind();

		cubeProg.bind(); {
			cubeProg.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			cubeProg.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
		} cubeProg.unbind();

		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		// TODO Auto-generated method stub

	}

}
