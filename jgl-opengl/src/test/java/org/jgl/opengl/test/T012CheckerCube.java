package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;

import java.nio.FloatBuffer;

import javax.media.opengl.GL3;

import org.jgl.geom.Cube;
import org.jgl.math.matrix.Matrix4;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T012CheckerCube extends GL3EventListener {

	private GLProgram p;
	private Cube cube = new Cube();
	private GLVertexArray cubeVao = new GLVertexArray();
	private GLBuffer cubeVerts;
	private GLBuffer cubeIndices;
	private GLBuffer cubeTexCoords;
	
	private Matrix4 cameraMatrix = new Matrix4();
	private Matrix4 perspMatrix = new Matrix4();
	private FloatBuffer perspMatrixBuffer = FloatBuffer.allocate(16);
	private FloatBuffer cameraMatrixBuffer = FloatBuffer.allocate(16); // TODO clean this up somehow
	private GLUniformAttribute cameraMatrixAttr;
	private GLUniformAttribute projectionMatrixAttr;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t012CheckerCube/checkerCube.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t012CheckerCube/checkerCube.fs", gl);
		
		cubeVerts = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3); // TODO tighter component size controls...
		cubeIndices = buffer(cube.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, 4);
		cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		cameraMatrixAttr = p.getUniformAttribute("CameraMatrix");
		projectionMatrixAttr = p.getUniformAttribute("ProjectionMatrix");
		
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
		gl.glClear(GL_DEPTH_BUFFER_BIT);
		gl.glDrawElements(GL_TRIANGLES, 
				cubeIndices.getRawBuffer().remaining(),
				cubeIndices.getGlBufferType(),
				cubeIndices.getRawBuffer());
		cubeVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.elapsedTimeUs * 0.000001;
		
		orbit(cameraMatrix, new Vector3(), 2.7, degrees(time * 135), degrees(sineWave(time / 20) * 90));
		cameraMatrixAttr.setMat4fv(true, cameraMatrixBuffer, cameraMatrix);
	}

	@Override
	protected void onResize(GL3 gl, GlViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, newViewport.width, newViewport.height);
		perspectiveX(perspMatrix, degrees(70), newViewport.width / newViewport.height, 1, 20);
		projectionMatrixAttr.setMat4fv(true, perspMatrixBuffer, perspMatrix);
	}
}