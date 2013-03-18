package org.jgl.opengl;

import static org.jgl.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;

import java.util.*;

public class GLProgram extends GLContextBoundResource {

	private List<GLShader> shaders = new ArrayList<GLShader>();
	
	public GLProgram attachShader(GLShader s) {
		checkNotNull(s);
		checkArgument(!shaders.contains(s), resourceMsg("Shader is already attached"));		
		shaders.add(s);
		return this;
	}
	
	private void link() {
		
		for (GLShader s : shaders) { 
			if (!s.isInitialized()) { s.init(getGl()); }
			getGl().glAttachShader(getGlResourceHandle(), s.getGlResourceHandle());
			checkError.apply(getGl());
		}
		
		getGl().glLinkProgram(getGlResourceHandle());
		int linkStatus = getGlslParam(this, GL_LINK_STATUS);
		
		if (linkStatus == GL_FALSE) {
			throw new IllegalStateException(resourceMsg(getGlslLog(this)));
		}
	}
	
	@Override
	protected void doInit() { 
		setGlResourceHandle(getGl().glCreateProgram());		
		link();
	}

	@Override
	protected void doBind() { getGl().glUseProgram(getGlResourceHandle()); }

	@Override
	protected void doUnbind() { getGl().glUseProgram(0); }

	@Override
	protected void doDestroy() { 
		for (GLShader s : shaders) { s.destroy(); }
		getGl().glDeleteProgram(getGlResourceHandle());
	}
}
