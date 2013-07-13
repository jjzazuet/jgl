package net.tribe7.mchochlik.test;

import static javax.media.opengl.GL.*;
import static net.tribe7.mchochlik.test.TGeometry.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatVec4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T005Manderblot extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("./src/test/resources/net/tribe7/mchochlik/test/t005Manderblot/manderblot.vs", 
				"./src/test/resources/net/tribe7/mchochlik/test/t005Manderblot/manderblot.fs", gl);

		rectVao.init(gl);
		p.bind(); {
			GLUFloatVec4 clrs = p.getVec4("clrs");

			p.getStageAttribute("Position").set(rectVao, 
					buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
					false, 0).enable();
			p.getStageAttribute("Coord").set(rectVao, 
					buffer(rectangle_coords, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
					false, 0).enable();
			clrs.set(0, color_map[0]);
			clrs.set(1, color_map[1]);
			clrs.set(2, color_map[2]);
			clrs.set(3, color_map[3]);
			clrs.set(4, color_map[4]);
		}
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		rectVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		rectVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);;
	}
}
