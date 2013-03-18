package org.jgl.opengl.test;

import static org.jgl.opengl.GLShaderType.*;
import static com.google.common.base.Throwables.*;
import java.io.File;

import javax.media.opengl.*;

import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class ShaderCompileTest extends GLScheduledEventListener {

	@Override
	protected void doInit(GLAutoDrawable gad) {
		
		try {
			File vsSrcFile = new File("../jgl-opengl/src/test/java/org/jgl/glsl/test/t00basic/basic.vs");
			File fsSrcFile = new File("../jgl-opengl/src/test/java/org/jgl/glsl/test/t00basic/basic.fs");
			
			GLShader vs = new GLShader(VERTEX_SHADER, vsSrcFile);
			GLShader fs = new GLShader(FRAGMENT_SHADER, fsSrcFile);
			GLProgram p = new GLProgram();
			
			p.attachShader(vs).attachShader(fs).init((GL3) gad.getGL());
			
		} catch (Exception e) { propagate(e); }
	}

	public void doRender(GLAutoDrawable gad, ExecutionState currentState) throws Exception {}
	protected void doUpdate(GLAutoDrawable gad, ExecutionState currentState) throws Exception {}
	protected void onResize(GLAutoDrawable gad, GlViewSize newViewport) {}
}
