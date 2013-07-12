package net.tribe7.opengl.test;

import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.perspectiveX;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.geom.solid.Cube;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T013StripedCubes extends GL3EventListener {

	private GLProgram p;
	private Cube cube = new Cube();
	private GLBuffer cubeVerts;
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix, uModelMatrix;

	private Angle fov = new Angle();
	private Angle azimuth = new Angle();
	private Angle elevation = new Angle();
	private Vector3 origin = new Vector3();

	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 camMat = new BufferedMatrix4();

	private ModelTransform cube1Trans = new ModelTransform();
	private ModelTransform cube2Trans = new ModelTransform();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		cubeVerts = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		
		p = loadProgram("./src/test/resources/net/tribe7/glsl/test/t013StripedCubes/stripedCubes.vs", 
				"./src/test/resources/net/tribe7/glsl/test/t013StripedCubes/stripedCubes.fs", gl);

		cubeVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(cubeVao, cubeVerts, false, 0).enable();
		p.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
		uProjectionMatrix = p.getMat4("ProjectionMatrix");
		uCameraMatrix = p.getMat4("CameraMatrix");
		uModelMatrix = p.getMat4("ModelMatrix");

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

		azimuth.setDegrees(time * 15);
		elevation.setDegrees(sineWave(time / 6.3) * 45);
		orbit(camMat, origin, 3.5, azimuth, elevation);
		uCameraMatrix.set(camMat);
		cube2Trans.getTranslation().set(1, 0, 0);
		cube2Trans.getRotationY().setDegrees(time * 90);
		cube1Trans.getTranslation().set(-1, 0, 0);
		cube1Trans.getRotationZ().setDegrees(time * 180);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMat, fov.setDegrees(60), newViewport.width / newViewport.height, 1, 30);
		uProjectionMatrix.set(projMat);
	}
}
