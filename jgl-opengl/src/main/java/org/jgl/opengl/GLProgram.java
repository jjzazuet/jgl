package org.jgl.opengl;

import static com.google.common.base.Charsets.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class GLProgram extends GLContextBoundResource {

	public static final String LEFT_BRACKET = "[";

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
			checkError().apply(getGl());
		}

		getGl().glLinkProgram(getGlResourceHandle());
		int linkStatus = getGlslParam(this, GL_LINK_STATUS);

		if (linkStatus == GL_FALSE) {
			throw new IllegalStateException(resourceMsg(getGlslLog(this)));
		}

		stageAttributes = extractAttributes(GL_ACTIVE_ATTRIBUTES);
		uniforms = extractAttributes(GL_ACTIVE_UNIFORMS);
	}

	private Map<String, GLAttribute> extractAttributes(int type) {

		checkArgument(type == GL_ACTIVE_UNIFORMS || type == GL_ACTIVE_ATTRIBUTES);

		int maxLength = getGlslParam(this, 
				type == GL_ACTIVE_UNIFORMS ? 
						GL_ACTIVE_UNIFORM_MAX_LENGTH : GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

		Map<String, GLAttribute> attributes = new HashMap<String, GLAttribute>();
		IntBuffer lengthBuf = IntBuffer.allocate(1);
		IntBuffer sizeBuf = IntBuffer.allocate(1);
		IntBuffer typeBuf = IntBuffer.allocate(1);
		ByteBuffer nameBuf;
		int attributeCount = getGlslParam(this, type);
		
		for (int k = 0; k < attributeCount; k++) {
			
			GLAttribute at;
			String attributeName;
			int location;
			
			lengthBuf.clear(); sizeBuf.clear(); typeBuf.clear();
			nameBuf = ByteBuffer.allocate(maxLength + 1);
			
			if (type == GL_ACTIVE_ATTRIBUTES) {
				
				getGl().glGetActiveAttrib(getGlResourceHandle(), k, nameBuf.limit(), 
						lengthBuf, sizeBuf, typeBuf, nameBuf);

				attributeName = UTF_8.decode(nameBuf).toString().trim();
				location = getGl().glGetAttribLocation(getGlResourceHandle(), attributeName);
				at = new GLVertexAttribute(k, location, sizeBuf.get(), typeBuf.get(), attributeName, this);
				
			} else {
				
				getGl().glGetActiveUniform(getGlResourceHandle(), k, nameBuf.limit(), 
						lengthBuf, sizeBuf, typeBuf, nameBuf);

				attributeName = UTF_8.decode(nameBuf).toString().trim();

				if (attributeName.contains(LEFT_BRACKET)) {
					attributeName = attributeName.substring(0, attributeName.indexOf(LEFT_BRACKET));
				}

				location = getGl().glGetUniformLocation(getGlResourceHandle(), attributeName);
				at = new GLUniformAttribute(k, location, sizeBuf.get(), typeBuf.get(), attributeName, this);
			}

			if (log.isDebugEnabled()) { log.debug(at.toString()); }
			attributes.put(at.getName(), at);
		}
		
		return attributes;
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
