package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static com.google.common.base.Throwables.propagate;
import static org.jgl.opengl.GLShaderType.*;
import static org.jgl.opengl.GLBufferFactory.*;

import java.io.File;
import javax.media.opengl.*;

import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T01Triangle extends GLScheduledEventListener {

	private GLVertexArray triangleVao = new GLVertexArray();
	private GLProgram p = new GLProgram();
	
	private float[] triangleVertices = new float[] { 
			0.0f, 0.0f, 0.0f, 
			1.0f, 0.0f, 0.0f, 
			0.0f, 1.0f, 0.0f 
	};

	@Override
	protected void doInit(GLAutoDrawable gad) {
		try {
			GL3 gl = (GL3) gad.getGL();
			File vsSrcFile = new File("../jgl-opengl/src/test/java/org/jgl/glsl/test/t01Triangle/triangle.vs");
			File fsSrcFile = new File("../jgl-opengl/src/test/java/org/jgl/glsl/test/t01Triangle/triangle.fs");

			GLShader vs = new GLShader(VERTEX_SHADER, vsSrcFile);
			GLShader fs = new GLShader(FRAGMENT_SHADER, fsSrcFile);
			GLBuffer triangleBuffer = buffer(triangleVertices, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);

			p.attachShader(vs).attachShader(fs).init(gl);
			
			GLVertexAttribute position = p.getStageAttribute("Position");
			
			triangleVao.init(gl);
			triangleVao.bindAttribute(position, triangleBuffer, 0);
			triangleVao.enable(position);
			
			gl.glClearColor(0, 0, 0, 1);
			gl.glClearDepth(1);
			
		} catch (Exception e) {
			propagate(e);
		}
	}

	@Override
	public void doRender(GLAutoDrawable gad, ExecutionState currentState) throws Exception {
		
		GL3 gl = (GL3) gad.getGL();
		
		p.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		triangleVao.bind();
		gl.glDrawArrays(GL_TRIANGLES, 0, 3);
		triangleVao.unbind();
		p.unbind();
	}

	@Override
	protected void doUpdate(GLAutoDrawable gad, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GLAutoDrawable gad, GlViewSize newViewport) {}
}
