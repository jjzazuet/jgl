package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;

import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.perspectiveX;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import org.jgl.geom.shape.Cube;
import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T010RgbCube extends GL3EventListener {

	private GLProgram p;
	private Cube cube = new Cube();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVerts, cubeNormals;
	
	private Angle xFov = new Angle();
	private BufferedMatrix4 cameraMatrix = new BufferedMatrix4();
	private BufferedMatrix4 projMatrix = new BufferedMatrix4();
	private GLUniformAttribute projectionMatrixAttr;

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t010RgbCube/rgbCube.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t010RgbCube/rgbCube.fs", gl);
		
		cubeVerts = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		projectionMatrixAttr = p.getUniformAttribute("ProjectionMatrix");
		
		cubeVao.init(gl);
		
		p.bind();
		p.getStageAttribute("Position").set(cubeVao, cubeVerts, false, 0).enable();
		p.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
		lookAt(cameraMatrix, new Vector3(2, 2, 2), new Vector3());
		p.getUniformAttribute("CameraMatrix").setMat4fv(false, cameraMatrix);

		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		cubeVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glDrawArrays(GL_TRIANGLES, 0, 36);
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		
		log.info(newViewport.toString());
		
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
		
		xFov.setDegrees(48);
		double ar = newViewport.width / newViewport.height;
		
		perspectiveX(projMatrix, xFov, ar, 1, 100);
		projectionMatrixAttr.setMat4fv(false, projMatrix);
	}
}
