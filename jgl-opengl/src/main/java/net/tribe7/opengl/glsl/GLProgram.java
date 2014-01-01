package net.tribe7.opengl.glsl;

import static net.tribe7.opengl.glsl.attribute.GLAttributeFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

import java.util.*;
import net.tribe7.opengl.GLContextBoundResource;

public class GLProgram extends GLContextBoundResource {

	private final List<GLShader> shaders = new ArrayList<GLShader>();
	private GLProgramInterface Interface = new GLProgramInterface();

	public GLProgram attachShader(GLShader s) {
		checkNotNull(s);
		checkState(!isInitialized(), resourceMsg("Program already initialized."));
		checkArgument(!shaders.contains(s), resourceMsg("Shader is already attached"));
		shaders.add(s);
		return this;
	}

	@Override
	protected int doInit() {

		setGlResourceHandle(getGl().glCreateProgram());

		for (GLShader s : shaders) { 
			s.init(getGl());
			getGl().glAttachShader(getGlResourceHandle(), s.getGlResourceHandle());
			checkError();
		}

		getGl().glLinkProgram(getGlResourceHandle());
		int linkStatus = getGlslParam(this, GL_LINK_STATUS);

		checkState(linkStatus != GL_FALSE, resourceMsg(getGlslLog(this)));
		getInterface().getUniformBlocks().putAll(getAttributeMap(GL_ACTIVE_UNIFORM_BLOCKS, this));
		getInterface().getUniforms().putAll(getAttributeMap(GL_ACTIVE_UNIFORMS, this));
		getInterface().getStageAttributes().putAll(getAttributeMap(GL_ACTIVE_ATTRIBUTES, this));

		return getGlResourceHandle();
}

	@Override
	protected void doBind() { 
		getGl().glUseProgram(getGlResourceHandle()); 
	}

	@Override
	protected void doUnbind() { 
		getGl().glUseProgram(ZERO); 
	}

	@Override
	protected void doDestroy() {
		for (GLShader s : shaders) { s.destroy(); }
		getGl().glDeleteProgram(getGlResourceHandle());
	}

	public GLProgramInterface getInterface() { return Interface; }
}
