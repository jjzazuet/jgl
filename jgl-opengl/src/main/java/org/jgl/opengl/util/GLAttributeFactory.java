package org.jgl.opengl.util;

import static com.google.common.base.Charsets.*;
import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLSLUtils.*;

import java.nio.*;
import java.util.*;

import org.jgl.opengl.glsl.*;
import org.jgl.opengl.glsl.attribute.*;
import org.slf4j.*;

public class GLAttributeFactory {

	public static final String LEFT_BRACKET = "[";
	private static final Logger log = LoggerFactory.getLogger(GLUniformAttribute.class);

	public static final Map<String, GLAttribute> getAttributeMap(int type, IntBuffer lengthBuf, IntBuffer sizeBuf, IntBuffer typeBuf, GLProgram p) {

		checkArgument(type == GL_ACTIVE_UNIFORMS || type == GL_ACTIVE_ATTRIBUTES);

		int maxLength = getGlslParam(p, type == GL_ACTIVE_UNIFORMS ? GL_ACTIVE_UNIFORM_MAX_LENGTH : GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		int attributeCount = getGlslParam(p, type);
		ByteBuffer nameBuf;
		Map<String, GLAttribute> programAttributes = new HashMap<String, GLAttribute>();

		for (int k = 0; k < attributeCount; k++) {

			GLAttribute at;
			String attributeName;
			int location;

			lengthBuf.clear(); sizeBuf.clear(); typeBuf.clear();
			nameBuf = ByteBuffer.allocate(maxLength + 1);

			if (type == GL_ACTIVE_ATTRIBUTES) {

				p.getGl().glGetActiveAttrib(p.getGlResourceHandle(), k, nameBuf.limit(), 
						lengthBuf, sizeBuf, typeBuf, nameBuf);

				attributeName = UTF_8.decode(nameBuf).toString().trim();
				location = p.getGl().glGetAttribLocation(p.getGlResourceHandle(), attributeName);
				at = new GLVertexAttribute(k, location, sizeBuf.get(), typeBuf.get(), attributeName, p);
				
			} else {

				p.getGl().glGetActiveUniform(p.getGlResourceHandle(), k, nameBuf.limit(), 
						lengthBuf, sizeBuf, typeBuf, nameBuf);

				attributeName = UTF_8.decode(nameBuf).toString().trim();

				if (attributeName.contains(LEFT_BRACKET)) {
					attributeName = attributeName.substring(0, attributeName.indexOf(LEFT_BRACKET));
				}

				location = p.getGl().glGetUniformLocation(p.getGlResourceHandle(), attributeName);
				int uniformType = typeBuf.get();

				switch (uniformType) {
					case GL_INT:
						at = new GLUInt(k, location, sizeBuf.get(), GL_INT, attributeName, p);
						break;
					case GL_FLOAT: 
						at = new GLUFloat(k, location, sizeBuf.get(), GL_FLOAT, attributeName, p);
						break;
					case GL_FLOAT_VEC2: 
						at = new GLUFloatVec2(k, location, sizeBuf.get(), GL_FLOAT_VEC2, attributeName, p);
						break;
					case GL_FLOAT_VEC3: 
						at = new GLUFloatVec3(k, location, sizeBuf.get(), GL_FLOAT_VEC3, attributeName, p);
						break;
					case GL_FLOAT_VEC4: 
						at = new GLUFloatVec4(k, location, sizeBuf.get(), GL_FLOAT_VEC4, attributeName, p);
						break;
					case GL_FLOAT_MAT2:
						at = new GLUFloatMat2(k, location, sizeBuf.get(), GL_FLOAT_MAT2, attributeName, p);
						break;
					case GL_FLOAT_MAT4:
						at = new GLUFloatMat4(k, location, sizeBuf.get(), GL_FLOAT_MAT4, attributeName, p);
						break;
					case GL_SAMPLER_2D:
						at = new GLUSampler2D(k, location, sizeBuf.get(), GL_SAMPLER_2D, attributeName, p);
						break;
					default: throw new IllegalStateException(
							String.format("Unsupported uniform type: [%s]", Integer.toHexString(uniformType)));
				}
			}

			if (log.isDebugEnabled()) { log.debug(at.toString()); }
			programAttributes.put(at.getName(), at);
		}

		return programAttributes;
	}
}
