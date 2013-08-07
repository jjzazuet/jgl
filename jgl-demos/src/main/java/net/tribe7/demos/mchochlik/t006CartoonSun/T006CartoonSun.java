package net.tribe7.demos.mchochlik.t006CartoonSun;

import static javax.media.opengl.GL.*;
import static net.tribe7.demos.mchochlik.TGeometry.rectangle_verts;
import static net.tribe7.opengl.GLBufferFactory.buffer;
import static net.tribe7.opengl.util.GLSLUtils.loadProgram;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.math.angle.Angle;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/006_cartoon_sun.png")
public class T006CartoonSun extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	private GLUFloat time;
	private GLUFloatVec2 sunPos;
	private Angle angle = new Angle();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		
		p = loadProgram("/net/tribe7/demos/mchochlik/t006CartoonSun/cartoonSun.vs", 
				"/net/tribe7/demos/mchochlik/t006CartoonSun/cartoonSun.fs", gl);

		rectVao.init(gl);
		p.bind(); {
			time = p.getInterface().getFloat("Time");
			sunPos = p.getInterface().getVec2("SunPos");
			p.getInterface().getStageAttribute("Position").set(rectVao, 
					buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
					false, 0).enable();
			p.getInterface().getVec3("Sun1").set(0.95f, 0.85f, 0.60f);
			p.getInterface().getVec3("Sun2").set(0.90f, 0.80f, 0.20f);
			p.getInterface().getVec3("Sky1").set(0.90f, 0.80f, 0.50f);
			p.getInterface().getVec3("Sky2").set(0.80f, 0.60f, 0.40f);
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
