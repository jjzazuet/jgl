package net.tribe7.demos.mchochlik.t013StripedCubes;

import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.perspectiveX;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Cube;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.opengl.*;
import net.tribe7.opengl.camera.GLPointCamera;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/013_striped_cubes.png")
public class T013StripedCubes extends GL3EventListener {

	private GLProgram p;
	private Cube cube = new Cube();
	private GLBuffer cubeVerts;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix, uModelMatrix;

	private ModelTransform cube1Trans = new ModelTransform();
	private ModelTransform cube2Trans = new ModelTransform();
	private GLPointCamera camera = new GLPointCamera(60);

	@Override
	protected void doInit(GL3 gl) throws Exception {

		cubeVerts = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		
		p = loadProgram("/net/tribe7/demos/mchochlik/t013StripedCubes/stripedCubes.vs", 
				"/net/tribe7/demos/mchochlik/t013StripedCubes/stripedCubes.fs", gl);

		cubeVao.init(gl);
		p.bind();
		p.getInterface().getStageAttribute("Position").set(cubeVao, cubeVerts, false, 0).enable();
		p.getInterface().getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		uModelMatrix = p.getInterface().getMat4("ModelMatrix");

		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

		getDrawHelper().glClearColor().glClearDepth();

		cubeVao.bind();
		uModelMatrix.set(cube1Trans.getModelMatrix());
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVerts.getRawBuffer().capacity());
		uModelMatrix.set(cube2Trans.getModelMatrix());
		gl.glDrawArrays(GL_TRIANGLES, 0, cubeVerts.getRawBuffer().capacity());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		camera.getAzimuth().setDegrees(time * 15);
		camera.getElevation().setDegrees(sineWave(time / 6.3) * 45);
		orbit(camera.getMatrix(), camera.getTarget(), 3.5, camera.getAzimuth(), camera.getElevation());
		uCameraMatrix.set(camera.getMatrix());
		cube2Trans.getTranslation().set(1, 0, 0);
		cube2Trans.getRotationY().setDegrees(time * 90);
		cube1Trans.getTranslation().set(-1, 0, 0);
		cube1Trans.getRotationZ().setDegrees(time * 180);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(camera.getProjection(), camera.getFov(), 
				newViewport.width / newViewport.height, 1, 30);
		uProjectionMatrix.set(camera.getProjection());
	}
}
