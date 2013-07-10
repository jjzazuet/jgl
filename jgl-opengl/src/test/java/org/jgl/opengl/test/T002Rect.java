package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.test.TGeometry.*;
import javax.media.opengl.GL3;

import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T002Rect extends GL3EventListener {

	private GLVertexArray rectVao = new GLVertexArray();
	private GLProgram p;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t002Rect/rect.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t002Rect/rect.fs", gl);

		rectVao.init(gl);
		p.bind(); {
			GLBuffer verts = buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2);
			GLBuffer colors = buffer(rectangle_colors, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
			p.getStageAttribute("Position").set(rectVao, verts, false, 0).enable();
			p.getStageAttribute("Color").set(rectVao, colors, false, 0).enable();		
		} p.unbind();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		p.bind();
		rectVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		rectVao.unbind();
		p.unbind();	
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
}
