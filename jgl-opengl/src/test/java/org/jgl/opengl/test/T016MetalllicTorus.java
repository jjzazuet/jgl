package org.jgl.opengl.test;

import static org.jgl.opengl.util.GLDrawUtils.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL3.*;

import javax.media.opengl.GL3;

import org.jgl.geom.solid.Torus;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T016MetalllicTorus extends GL3EventListener {

	private GLProgram p;
	
	private Torus torus = new Torus(1.0, 0.5, 72, 48);
	private GLVertexArray torusVao = new GLVertexArray();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016MetallicTorus/metallicTorus.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t016MetallicTorus/metallicTorus.fs", gl);
		
		torusVao.init(gl);
		p.bind();
		
		gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_CULL_FACE);
		glFrontFace(gl, torus.getFaceWinding());
		gl.glCullFace(GL_BACK);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {

	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {

	}
}