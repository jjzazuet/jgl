package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.math.matrix.Matrix4OpsCam.*;
import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.angle.AngleOps.*;
import static org.jgl.math.vector.VectorOps.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Cube;
import org.jgl.geom.solid.Torus;
import org.jgl.opengl.GL3EventListener;
import org.jgl.opengl.GLBuffer;
import org.jgl.opengl.GLVertexArray;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.GLShader;
import org.jgl.opengl.util.GLBufferUtils;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T025RenderedTexture extends GL3EventListener {

	private Cube cube = new Cube();
	private Torus torus = new Torus(1.0, 0.5, 72, 48);
	private GLProgram cubeProgram;
	private GLProgram torusProgram;

	private GLVertexArray cubeVao = new GLVertexArray();
	private GLVertexArray torusVao = new GLVertexArray();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		GLShader vertexShader = loadVertexShader("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025renderedTexture/renderedTexture.vs");
		GLShader cubeFragmentShader = loadFragmentShader("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025renderedTexture/cube.fs");
		GLShader torusFragmentShader = loadFragmentShader("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t025renderedTexture/torus.fs");

		cubeProgram = new GLProgram().attachShader(vertexShader).attachShader(cubeFragmentShader);
		torusProgram = new GLProgram().attachShader(vertexShader).attachShader(torusFragmentShader);

		cubeProgram.init(gl);
		cubeVao.init(gl);
		torusProgram.init(gl);
		torusVao.init(gl);

		cubeProgram.bind(); {
			GLBuffer cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			cubeProgram.getStageAttribute("Position").set(cubeVao, cubeVertices, false, 0).enable();
			cubeProgram.getStageAttribute("Normal").set(cubeVao, cubeNormals, false, 0).enable();
			cubeProgram.getStageAttribute("TexCoord").set(cubeVao, cubeTexCoords, false, 0).enable();
			cubeProgram.getVec3("LightPos").set(4.0f, 4.0f, -8.0f);
		}
		cubeProgram.unbind();

		torusProgram.bind(); {
			GLBuffer torusVertices = buffer(torus.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer torusNormals = buffer(torus.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
			GLBuffer torusTexCoords = buffer(torus.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

			torusProgram.getStageAttribute("Position").set(torusVao, torusVertices, false, 0).enable();
			torusProgram.getStageAttribute("Normal").set(torusVao, torusNormals, false, 0).enable();
			torusProgram.getStageAttribute("TexCoord").set(torusVao, torusTexCoords, false, 0).enable();
			torusProgram.getVec3("LightPos").set(2.0f, 3.0f, 4.0f);
		}
		torusProgram.unbind();
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {

	}
}
