package net.tribe7.demos.mchochlik.t027DepthOfField;

import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.opengl.GL3EventListener;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/027_depth_of_field.png")
public class T027DepthOfField extends GL3EventListener {

	private GLProgram mainProg, dofProg;

	@Override
	protected void doInit(GL3 gl) throws Exception {
		mainProg = loadProgram("/net/tribe7/demos/mchochlik/t027DepthOfField/main.vs", 
				"/net/tribe7/demos/mchochlik/t027DepthOfField/main.fs", gl);
		dofProg = loadProgram("/net/tribe7/demos/mchochlik/t027DepthOfField/dof.vs", 
				"/net/tribe7/demos/mchochlik/t027DepthOfField/dof.fs", gl);
		log.info("");
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		// TODO Auto-generated method stub
	}
}
