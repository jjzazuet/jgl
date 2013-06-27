package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.test.TGeometry.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import javax.media.opengl.GL3;

import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUniformAttribute;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T005Manderblot extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t005Manderblot/manderblot.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t005Manderblot/manderblot.fs", gl);
		
		GLUniformAttribute clrs = p.getUniformAttribute("clrs");

		p.bind();
		rectVao.init(gl);
		p.getStageAttribute("Position").set(rectVao, 
				buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
				false, 0).enable();
		p.getStageAttribute("Coord").set(rectVao, 
				buffer(rectangle_coords, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
				false, 0).enable();
		clrs.setVec4fv(color_map);
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		rectVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		rectVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
}
