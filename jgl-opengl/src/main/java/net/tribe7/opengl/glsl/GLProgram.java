package net.tribe7.opengl.glsl;

import static net.tribe7.opengl.util.GLAttributeFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.IntBuffer;
import java.util.*;

import net.tribe7.opengl.GLContextBoundResource;
import net.tribe7.opengl.glsl.attribute.*;

public class GLProgram extends GLContextBoundResource {

	private List<GLShader> shaders = new ArrayList<GLShader>();
	private Map<String, GLAttribute> uniforms = new HashMap<String, GLAttribute>();
	private Map<String, GLAttribute> stageAttributes = new HashMap<String, GLAttribute>();

	public GLProgram attachShader(GLShader s) {
		checkNotNull(s);
		checkState(!isInitialized(), resourceMsg("Program already initialized."));
		checkArgument(!shaders.contains(s), resourceMsg("Shader is already attached"));
		shaders.add(s);
		return this;
	}

	private void link() {

		for (GLShader s : shaders) { 
			s.init(getGl());
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

		getAttributeMap(GL_ACTIVE_UNIFORM_BLOCKS, lengthBuf, sizeBuf, typeBuf, this);
		stageAttributes = getAttributeMap(GL_ACTIVE_ATTRIBUTES, lengthBuf, sizeBuf, typeBuf, this);
		uniforms = getAttributeMap(GL_ACTIVE_UNIFORMS, lengthBuf, sizeBuf, typeBuf, this);
	}

	public GLVertexAttribute getStageAttribute(String name) {
		checkInitialized();
		checkArgument(stageAttributes.get(name) != null);
		return (GLVertexAttribute) stageAttributes.get(name); 
	}
	
	public GLUInt getInt(String name) {
		checkNameAccess(name);
		return (GLUInt) uniforms.get(name);
	}
	
	public GLUFloat getFloat(String name) {
		checkNameAccess(name);
		return (GLUFloat) uniforms.get(name);
	}

	public GLUFloatVec2 getVec2(String name) {
		checkNameAccess(name);
		return (GLUFloatVec2) uniforms.get(name);
	}

	public GLUFloatVec3 getVec3(String name) {
		checkNameAccess(name);
		return (GLUFloatVec3) uniforms.get(name);
	}

	public GLUFloatVec4 getVec4(String name) {
		checkNameAccess(name);
		return (GLUFloatVec4) uniforms.get(name);
	}

	public GLUFloatMat2 getMat2(String name) {
		checkNameAccess(name);
		return (GLUFloatMat2) uniforms.get(name);
	}

	public GLUFloatMat4 getMat4(String name) {
		checkNameAccess(name);
		return (GLUFloatMat4) uniforms.get(name);
	}

	public GLUSampler getSampler(String name) {
		checkNameAccess(name);
		return (GLUSampler) uniforms.get(name);
	}

	protected void checkNameAccess(String name) {
		checkInitialized();
		checkNotNull(uniforms.get(name));
	}

	@Override
	protected void doInit() { 
		setGlResourceHandle(getGl().glCreateProgram());
		link();
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
}
