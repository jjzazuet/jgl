package net.tribe7.demos.mchochlik.t004Rect;

import static javax.media.opengl.GL3.*;
import static javax.media.opengl.GL.GL_ARRAY_BUFFER;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_STATIC_DRAW;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static net.tribe7.opengl.GLBufferFactory.*;

import javax.media.opengl.GL3;

import net.tribe7.geom.io.GeometryBuffer;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.util.*;
import net.tribe7.time.util.ExecutionState;

public class T004Rect extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	private GeometryBuffer<Float> rectVertices = new GeometryBuffer<Float>(2, 
		new Float [] {
		-1.0f, -1.0f,
		-1.0f,  1.0f,
		 1.0f, -1.0f,
		 1.0f,  1.0f
	});

	@Override
	protected void doInit(GL3 gl) throws Exception {
		p = loadProgram("/net/tribe7/demos/mchochlik/t004Rect/rect.vs", 
				"/net/tribe7/demos/mchochlik/t004Rect/rect.fs", gl);
		rectVao.init(gl);
		GLBuffer rectVerts = buffer(rectVertices, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		p.bind(); {
			p.getInterface().getStageAttribute("Position").set(rectVao, rectVerts, false, 0).enable();
			p.getInterface().getVec2("RedCenter").set(-0.141f, 0.141f);
			p.getInterface().getVec2("GreenCenter").set(0.141f, 0.141f);
			p.getInterface().getVec2("BlueCenter").set(0.0f, -0.2f);
		} p.unbind();
		gl.glDisable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		getDrawHelper().glClearColor();
		p.bind(); {
			rectVao.bind();
			gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			rectVao.unbind();
		} p.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort((int) newViewport.width, (int) newViewport.height);
	}
}
