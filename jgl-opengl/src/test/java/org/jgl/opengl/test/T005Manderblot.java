package org.jgl.opengl.test;

import static org.jgl.opengl.util.GLSLUtils.*;
import javax.media.opengl.GL3;

import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T005Manderblot extends GL3EventListener {

	private GLProgram p;
	
	@Override
	protected void doInit(GL3 gl) throws Exception {
		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t005Manderblot/manderblot.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t005Manderblot/manderblot.fs", gl);
		log.info("aaaaaaaa");
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
	}

	@Override
	protected void onResize(GL3 gl, GlViewSize newViewport) {
	}
}
