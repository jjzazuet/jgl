package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.buffer;
import static org.jgl.opengl.test.TGeometry.rectangle_verts;
import static org.jgl.opengl.util.GLSLUtils.loadProgram;

import javax.media.opengl.GL3;

import org.jgl.math.angle.Angle;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T006CartoonSun extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	private GLUFloat time;
	private GLUFloatVec2 sunPos;
	private Angle angle = new Angle();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("./src/test/resources/org/jgl/glsl/test/t006CartoonSun/cartoonSun.vs", 
				"./src/test/resources/org/jgl/glsl/test/t006CartoonSun/cartoonSun.fs", gl);

		rectVao.init(gl);
		p.bind(); {
			time = p.getFloat("Time");
			sunPos = p.getVec2("SunPos");
			p.getStageAttribute("Position").set(rectVao, 
					buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
					false, 0).enable();
			p.getVec3("Sun1").set(0.95f, 0.85f, 0.60f);
			p.getVec3("Sun2").set(0.90f, 0.80f, 0.20f);
			p.getVec3("Sky1").set(0.90f, 0.80f, 0.50f);
			p.getVec3("Sky2").set(0.80f, 0.60f, 0.40f);
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
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		double uTime = currentState.getElapsedTimeSeconds();
		angle.setFullCircles(uTime * 0.05);
		time.set((float) uTime);
		sunPos.set(-angle.cos(), angle.sin());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
	}
}
