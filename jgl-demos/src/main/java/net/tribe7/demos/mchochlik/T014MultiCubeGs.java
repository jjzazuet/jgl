package net.tribe7.demos.mchochlik;

import static net.tribe7.math.angle.AngleOps.sineWave;
import static net.tribe7.math.matrix.Matrix4OpsCam.orbit;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Cube;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/014_multi_cube_gs.png")
public class T014MultiCubeGs extends GL3EventListener {

	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Vector3 origin = new Vector3();
	private Cube cube = new Cube();
	private GLProgram p;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix;
	private GLBuffer cubeVertices;
	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 camMat = new BufferedMatrix4();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		
		p = loadProgram("/net/tribe7/demos/mchochlik/t014MultiCubeGs/multiCube.vs", 
				"/net/tribe7/demos/mchochlik/t014MultiCubeGs/multiCube.gs",
				"/net/tribe7/demos/mchochlik/t014MultiCubeGs/multiCube.fs", gl);

		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		uCameraMatrix = p.getMat4("CameraMatrix");

		cubeVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();

		gl.glClearColor(0.9f, 0.9f, 0.9f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		cubeVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVertices.getRawBuffer().capacity());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();
		
		azimuth.setDegrees(time * 135);
		elevation.setDegrees(sineWave(time / 20) * 30);
		orbit(camMat, origin, 18.5, azimuth, elevation);
		uCameraMatrix.set(camMat);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMat, fov.setDegrees(70), newViewport.width / newViewport.height, 1, 50);
		uProjectionMatrix.set(projMat);
	}
}
