package net.tribe7.demos.mchochlik.t015ShadedCube;

import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Cube;
import net.tribe7.math.angle.Angle;
import net.tribe7.opengl.*;
import net.tribe7.opengl.camera.GLPointCamera;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/015_shaded_cube.png")
public class T015ShadedCube extends GL3EventListener {

	private Cube cube = new Cube();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVertices, cubeNormals;
	private GLProgram p;

	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix;
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private GLPointCamera camera = new GLPointCamera(60);

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t015ShadedCube/shadedCube.vs", 
				"/net/tribe7/demos/mchochlik/t015ShadedCube/shadedCube.fs", gl);

		cubeVao.init(gl);
		p.bind();

		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		p.getInterface().getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
		p.getInterface().getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();

		gl.glClearColor(0.03f, 0.03f, 0.03f, 0.0f);
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
		orbit(camera.getMatrix(), camera.getTarget(), 3, 
				azimuth.setDegrees(time * 135), 
				elevation.setDegrees(sineWave(time / 20) * 90));
		uCameraMatrix.set(camera.getMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(camera.getProjection(), camera.getFov(), 
				newViewport.width / newViewport.height, 1, 20);
		uProjectionMatrix.set(camera.getProjection());
	}
}