package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.test.TGeometry.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import javax.media.opengl.GL3;

import org.jgl.math.vector.Vector3;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T004Newton extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t004Newton/newton.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t004Newton/newton.fs", gl);
		
		GLBuffer rectangleBuffer = buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2);
		
		rectVao.init(gl);
		p.bind();
		p.getStageAttribute("Position").set(rectVao, rectangleBuffer, false, 0).enable();
		p.getUniformAttribute("Color1").setVec3f(new Vector3(0.2f, 0.02f, 0.05f));
		p.getUniformAttribute("Color2").setVec3f(new Vector3(1.0f, 0.98f, 0.98f));
		gl.glDisable(GL_DEPTH_TEST);	
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
	protected void onResize(GL3 gl, GlViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
}
