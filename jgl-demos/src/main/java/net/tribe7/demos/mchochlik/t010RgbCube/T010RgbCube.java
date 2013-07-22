package net.tribe7.demos.mchochlik.t010RgbCube;

import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.perspectiveX;
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
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/010_rgb_cube.png")
public class T010RgbCube extends GL3EventListener {

	private GLProgram p;
	private Cube cube = new Cube();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVerts, cubeNormals;
	
	private Angle xFov = new Angle();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private GLUFloatMat4 projectionMatrixAttr;

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t010RgbCube/rgbCube.vs", 
				"/net/tribe7/demos/mchochlik/t010RgbCube/rgbCube.fs", gl);

		cubeVao.init(gl);
		p.bind(); {
			cubeVerts = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			projectionMatrixAttr = p.getMat4("ProjectionMatrix");
			p.getStageAttribute("Position").set(cubeVao, cubeVerts, false, 0).enable();
			p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			lookAt(cameraMatrix, new Vector3(2, 2, 2), new Vector3());
			p.getMat4("CameraMatrix").set(cameraMatrix);
		}

		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
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
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		xFov.setDegrees(48);
		perspectiveX(projMatrix, xFov, newViewport.aspectRatio, 1, 100);
		projectionMatrixAttr.set(projMatrix);
	}
}
