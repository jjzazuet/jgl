package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.opengl.glsl.attribute.GLAttributeBuffers.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.math.Preconditions.checkNoNulls;
import static net.tribe7.opengl.util.GLSLUtils.getGlslParam;

import org.slf4j.*;

import net.tribe7.opengl.glsl.GLProgram;

public class GLUniformAttributeFactory {

	private static final Logger log = LoggerFactory.getLogger(GLUniformAttributeFactory.class);

	protected static GLUniformAttribute<?> newUniformAttribute(int index, boolean ignoreLocation, GLProgram p) {

		GLUniformAttribute<?> at = null;
		GLAttributeBuffers ab = new GLAttributeBuffers(index, getGlslParam(p, GL_ACTIVE_UNIFORM_MAX_LENGTH));

		p.getGl().glGetActiveUniform(p.getGlResourceHandle(), ab.getAttributeIndex(), ab.getName().limit(), ab.getLength(), ab.getSize(), ab.getType(), ab.getName());
		p.checkError();
		int location = p.getGl().glGetUniformLocation(p.getGlResourceHandle(), ab.getNameValue());
		p.checkError();

		if (location < ZERO && !ignoreLocation) {
			log.warn("Invalid uniform attribute location [{}: {}]", ab.getNameValue(), location);
		} else {
			at = newInstance(index, ab.getLengthValue(), location, ab.getSizeValue(), ab.getTypeValue(), ab.getNameValue(), p);
		}
		return at;
	}

	public static final GLUniformAttribute<?> newInstance(int index, int length, int location, int size, int glType, String name, GLProgram p) {

		checkNoNulls(name, p);
		GLUniformAttribute<?> at = null;

		switch (glType) {
			case GL_INT:
			case GL_UNSIGNED_INT:
				at = new GLUInt(index, location, size, glType, name, p);
				break;
			case GL_FLOAT: 
				at = new GLUFloat(index, location, size, GL_FLOAT, name, p);
				break;
			case GL_FLOAT_VEC2: 
				at = new GLUFloatVec2(index, location, size, GL_FLOAT_VEC2, name, p);
				break;
			case GL_FLOAT_VEC3: 
				at = new GLUFloatVec3(index, location, size, GL_FLOAT_VEC3, name, p);
				break;
			case GL_FLOAT_VEC4: 
				at = new GLUFloatVec4(index, location, size, GL_FLOAT_VEC4, name, p);
				break;
			case GL_FLOAT_MAT2:
				at = new GLUFloatMat2(index, location, size, GL_FLOAT_MAT2, name, p);
				break;
			case GL_FLOAT_MAT4:
				at = new GLUFloatMat4(index, location, size, GL_FLOAT_MAT4, name, p);
				break;
			case GL_SAMPLER_2D:
			case GL_SAMPLER_2D_RECT:
			case GL_SAMPLER_CUBE:
				at = new GLUSampler(index, location, size, glType, name, p);
				break;
			default: throw new IllegalStateException(
					String.format("Unsupported uniform type: [%s]", Integer.toHexString(glType)));
			}
		return at;
	}
}
