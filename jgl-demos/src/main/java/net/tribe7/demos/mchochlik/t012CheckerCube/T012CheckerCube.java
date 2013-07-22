package net.tribe7.demos.mchochlik.t012CheckerCube;

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
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/012_checker_cube.png")
public class T012CheckerCube extends GL3EventListener {

	private GLProgram p;
	private Cube cube = new Cube();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVerts;
	private GLBuffer cubeTexCoords;
	
	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private GLUFloatMat4 cameraMatrixAttr;
	private GLUFloatMat4 projectionMatrixAttr;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t012CheckerCube/checkerCube.vs", 
				"/net/tribe7/demos/mchochlik/t012CheckerCube/checkerCube.fs", gl);

		cubeVerts = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		cameraMatrixAttr = p.getMat4("CameraMatrix");
		projectionMatrixAttr = p.getMat4("ProjectionMatrix");

		cubeVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(cubeVao, cubeVerts, false, 0).enable();
		p.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		cubeVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVerts.getRawBuffer().capacity());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		azimuth.setDegrees(time * 135);
		elevation.setDegrees(sineWave(time / 20) * 90);
		orbit(cameraMatrix, new Vector3(), 2.7, azimuth, elevation);
		cameraMatrixAttr.set(cameraMatrix);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMatrix, fov.setDegrees(70), newViewport.width / newViewport.height, 1, 20);
		projectionMatrixAttr.set(projMatrix);
	}
}