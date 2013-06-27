package org.jgl.opengl.glsl;

import static org.jgl.opengl.util.GLAttributeFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;

import java.nio.IntBuffer;
import java.util.*;

import org.jgl.opengl.GLContextBoundResource;
import org.jgl.opengl.glsl.attribute.GLAttribute;
import org.jgl.opengl.glsl.attribute.GLUniformAttribute;
import org.jgl.opengl.glsl.attribute.GLVertexAttribute;

public class GLProgram extends GLContextBoundResource {

	private List<GLShader> shaders = new ArrayList<GLShader>();
	private Map<String, GLAttribute> uniforms = new HashMap<String, GLAttribute>();
	private Map<String, GLAttribute> stageAttributes = new HashMap<String, GLAttribute>();

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
			checkError();
		}

		getGl().glLinkProgram(getGlResourceHandle());
		int linkStatus = getGlslParam(this, GL_LINK_STATUS);

		if (linkStatus == GL_FALSE) {
			throw new IllegalStateException(resourceMsg(getGlslLog(this)));
		}

		IntBuffer lengthBuf = IntBuffer.allocate(1);
		IntBuffer sizeBuf = IntBuffer.allocate(1);
		IntBuffer typeBuf = IntBuffer.allocate(1);

		stageAttributes = getAttributeMap(GL_ACTIVE_ATTRIBUTES, lengthBuf, sizeBuf, typeBuf, this);
		uniforms = getAttributeMap(GL_ACTIVE_UNIFORMS, lengthBuf, sizeBuf, typeBuf, this);
	}

	public GLVertexAttribute getStageAttribute(String name) {
		checkInitialized();
		checkArgument(stageAttributes.get(name) != null);
		return (GLVertexAttribute) stageAttributes.get(name); 
	}
	
	public GLUniformAttribute getUniformAttribute(String name) {
		checkInitialized();
		checkArgument(uniforms.get(name) != null);
		return (GLUniformAttribute) uniforms.get(name);
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
