package net.tribe7.demos.mchochlik;

import static javax.media.opengl.GL.*;
import static net.tribe7.demos.mchochlik.TGeometry.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T002Rect extends GL3EventListener {

	private GLVertexArray rectVao = new GLVertexArray();
	private GLProgram p;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("/net/tribe7/demos/mchochlik/t002Rect/rect.vs", 
				"/net/tribe7/demos/mchochlik/t002Rect/rect.fs", gl);

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
