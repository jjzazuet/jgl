package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.test.TGeometry.*;
import static org.jgl.opengl.util.GLSLUtils.*;

import javax.media.opengl.*;

import org.jgl.opengl.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T001Triangle extends GL3EventListener {

	private GLVertexArray triangleVao = new GLVertexArray();
	private GLProgram p;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t001Triangle/triangle.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t001Triangle/triangle.fs", gl);

		GLBuffer triangleBuffer = buffer(triangleVertices, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		
		triangleVao.init(gl);
		p.getStageAttribute("Position").set(triangleVao, triangleBuffer, false, 0).enable();
		gl.glClearColor(0, 0, 0, 1);
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		p.bind();
		triangleVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glDrawArrays(GL_TRIANGLES, 0, 3);
		triangleVao.unbind();
		p.unbind();	
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}
	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {}
}
