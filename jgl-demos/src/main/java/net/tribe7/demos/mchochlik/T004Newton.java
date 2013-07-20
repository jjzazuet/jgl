package net.tribe7.demos.mchochlik;

import static javax.media.opengl.GL.*;
import static net.tribe7.demos.mchochlik.TGeometry.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/004_newton.png")
public class T004Newton extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t004Newton/newton.vs", 
				"/net/tribe7/demos/mchochlik/t004Newton/newton.fs", gl);

		rectVao.init(gl);
		p.bind(); {
			GLBuffer rectangleBuffer = buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2);
			p.getStageAttribute("Position").set(rectVao, rectangleBuffer, false, 0).enable();
			p.getVec3("Color1").set(new Vector3(0.2f, 0.02f, 0.05f));
			p.getVec3("Color2").set(new Vector3(1.0f, 0.98f, 0.98f));
		}
		gl.glDisable(GL_DEPTH_TEST);
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
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
}
