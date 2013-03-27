package org.jgl.opengl.test;

import static org.jgl.math.angle.AngleOps.*;
import static java.lang.Math.*;
import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.buffer;
import static org.jgl.opengl.test.TGeometry.rectangle_verts;
import static org.jgl.opengl.util.GLSLUtils.loadProgram;

import javax.media.opengl.GL3;

import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T006CartoonSun extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	private GLUniformAttribute time;
	private GLUniformAttribute sunPos;
	private double angle = 0;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t006CartoonSun/cartoonSun.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t006CartoonSun/cartoonSun.fs", gl);
		
		time = p.getUniformAttribute("Time");
		sunPos = p.getUniformAttribute("SunPos");
		
		p.bind();
		rectVao.init(gl);
		p.getStageAttribute("Position").set(rectVao, 
				buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
				false, 0).enable();
		p.getUniformAttribute("Sun1").setVec3f(0.95f, 0.85f, 0.60f);
		p.getUniformAttribute("Sun2").setVec3f(0.90f, 0.80f, 0.20f);
		p.getUniformAttribute("Sky1").setVec3f(0.90f, 0.80f, 0.50f);
		p.getUniformAttribute("Sky2").setVec3f(0.80f, 0.60f, 0.40f);
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
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		
		double uTime = currentState.elapsedTimeUs * 0.000001;
		angle = fullCircles(uTime * 0.05);
		
		time.setFloat((float) uTime);
		sunPos.setVec2f(-cos(angle), sin(angle));
	}

	@Override
	protected void onResize(GL3 gl, GlViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
}
