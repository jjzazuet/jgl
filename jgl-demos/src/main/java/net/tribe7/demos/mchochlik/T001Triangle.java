package net.tribe7.demos.mchochlik;

import static javax.media.opengl.GL.*;
import static net.tribe7.demos.mchochlik.TGeometry.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.*;

import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T001Triangle extends GL3EventListener {

	private GLVertexArray triangleVao = new GLVertexArray();
	private GLProgram p;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t001Triangle/triangle.vs", 
				"/net/tribe7/demos/mchochlik/t001Triangle/triangle.fs", gl);

		triangleVao.init(gl);
		p.bind(); {
			GLBuffer triangleBuffer = buffer(triangleVertices, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
			p.getStageAttribute("Position").set(triangleVao, triangleBuffer, false, 0).enable();
		} p.unbind();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		p.bind();
		triangleVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_TRIANGLES, 0, 3);
		triangleVao.unbind();
		p.unbind();	
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}
	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {}
}
