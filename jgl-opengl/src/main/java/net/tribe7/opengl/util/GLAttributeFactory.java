package net.tribe7.opengl.util;

import static net.tribe7.math.Preconditions.*;
import static java.lang.String.format;
import static net.tribe7.common.base.Charsets.*;
import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import java.nio.*;
import java.util.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.glsl.attribute.*;
import org.slf4j.*;

public class GLAttributeFactory {

	public static final int MINUS_ONE = -1;
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final String LEFT_BRACKET = "[";
	private static final Logger log = LoggerFactory.getLogger(GLUniformAttribute.class);

	protected static ByteBuffer newStringBuffer(int maxLength) {
		return ByteBuffer.allocate(maxLength + 1);
	}

	protected static String decodeUtf8(ByteBuffer b) {
		checkNotNull(b);
		return UTF_8.decode(b).toString().trim();
	}

	protected static GLVertexAttribute newStageAttribute(int index, IntBuffer lengthBuf, IntBuffer sizeBuf, IntBuffer typeBuf, GLProgram p) {
		checkNoNulls(lengthBuf, sizeBuf, typeBuf, p);
		GLVertexAttribute at = null;
		ByteBuffer nameBuf = newStringBuffer(getGlslParam(p, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH));

		p.getGl().glGetActiveAttrib(p.getGlResourceHandle(), index, nameBuf.limit(), lengthBuf, sizeBuf, typeBuf, nameBuf);
		p.checkError();
		String attributeName = decodeUtf8(nameBuf);
		int location = p.getGl().glGetAttribLocation(p.getGlResourceHandle(), attributeName);

		if (location < ZERO) {
			log.warn(p.resourceMsg(format("Invalid active attribute: [%s, %s, %s, %s]", 
					location, sizeBuf.get(), Integer.toHexString(typeBuf.get()), attributeName)));
		} else {
			at = new GLVertexAttribute(index, location, sizeBuf.get(), typeBuf.get(), attributeName, p);
		}
		return at;
	}

	protected static GLUniformAttribute<?> newUniformAttribute(int index, IntBuffer lengthBuf, IntBuffer sizeBuf, IntBuffer typeBuf, GLProgram p) {

		checkNoNulls(lengthBuf, sizeBuf, typeBuf, p);
		GLUniformAttribute<?> at = null;
		ByteBuffer nameBuf = newStringBuffer(getGlslParam(p, GL_ACTIVE_UNIFORM_MAX_LENGTH));

		p.getGl().glGetActiveUniform(p.getGlResourceHandle(), index, nameBuf.limit(), lengthBuf, sizeBuf, typeBuf, nameBuf);
		p.checkError();
		String attributeName = decodeUtf8(nameBuf);

		if (attributeName.contains(LEFT_BRACKET)) {
			attributeName = attributeName.substring(0, attributeName.indexOf(LEFT_BRACKET));
		}

		int location = p.getGl().glGetUniformLocation(p.getGlResourceHandle(), attributeName);
		int uniformType = typeBuf.get();

		if (location < ZERO) {
			log.warn(format("Invalid uniform attribute location [%s: %s]", attributeName, location));
		} else {
			switch (uniformType) {
				case GL_INT:
				case GL_UNSIGNED_INT:
					at = new GLUInt(index, location, sizeBuf.get(), uniformType, attributeName, p);
					break;
				case GL_FLOAT: 
					at = new GLUFloat(index, location, sizeBuf.get(), GL_FLOAT, attributeName, p);
					break;
				case GL_FLOAT_VEC2: 
					at = new GLUFloatVec2(index, location, sizeBuf.get(), GL_FLOAT_VEC2, attributeName, p);
					break;
				case GL_FLOAT_VEC3: 
					at = new GLUFloatVec3(index, location, sizeBuf.get(), GL_FLOAT_VEC3, attributeName, p);
					break;
				case GL_FLOAT_VEC4: 
					at = new GLUFloatVec4(index, location, sizeBuf.get(), GL_FLOAT_VEC4, attributeName, p);
					break;
				case GL_FLOAT_MAT2:
					at = new GLUFloatMat2(index, location, sizeBuf.get(), GL_FLOAT_MAT2, attributeName, p);
					break;
				case GL_FLOAT_MAT4:
					at = new GLUFloatMat4(index, location, sizeBuf.get(), GL_FLOAT_MAT4, attributeName, p);
					break;
				case GL_SAMPLER_2D:
				case GL_SAMPLER_2D_RECT:
				case GL_SAMPLER_CUBE:
					at = new GLUSampler(index, location, sizeBuf.get(), uniformType, attributeName, p);
					break;
				default: throw new IllegalStateException(
						String.format("Unsupported uniform type: [%s]", Integer.toHexString(uniformType)));
			}
		}

		return at;
	}

	protected static final int [] getUniformBlockParameter(GLProgram p, int uniformBlockIndex, int glParam, int paramCount) {
		checkNotNull(p);
		checkArgument(uniformBlockIndex >= ZERO);
		checkArgument(paramCount > ZERO);

		int [] paramData = new int[paramCount];
		IntBuffer pData = IntBuffer.wrap(paramData);
		p.getGl().glGetActiveUniformBlockiv(p.getGlResourceHandle(), uniformBlockIndex, glParam, pData);
		p.checkError();
		return paramData;
	}

	protected static final int [] getUniformBlockIndexParameter(GLProgram p, int glParam, int [] uniformIndices) {
		checkNotNull(p, uniformIndices);
		checkArgument(uniformIndices.length > ZERO);

		int [] paramData = new int[uniformIndices.length];
		IntBuffer pData = IntBuffer.wrap(paramData);
		IntBuffer uIndices = IntBuffer.wrap(uniformIndices);
		p.getGl().glGetActiveUniformsiv(p.getGlResourceHandle(), uniformIndices.length, uIndices, glParam, pData);
		p.checkError();
		return paramData;
	}

	protected static GLAttribute newUniformAttributeBlock(int index, IntBuffer lengthBuf, IntBuffer sizeBuf, IntBuffer typeBuf, GLProgram p) {

		checkNoNulls(lengthBuf, sizeBuf, typeBuf, p);

		GLAttribute at = null;
		int blockNameSize = getUniformBlockParameter(p, index, GL_UNIFORM_BLOCK_NAME_LENGTH, ONE)[0];
		ByteBuffer nameBuf = newStringBuffer(blockNameSize);

		p.getGl().glGetActiveUniformBlockName(p.getGlResourceHandle(), index, nameBuf.limit(), lengthBuf, nameBuf);
		p.checkError();
		String blockName = decodeUtf8(nameBuf);

		int blockSize = getUniformBlockParameter(p, index, GL_UNIFORM_BLOCK_DATA_SIZE, ONE)[0];
		int uniformCount = getUniformBlockParameter(p, index, GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS, ONE)[0];
		int [] uniformIndices = getUniformBlockParameter(p, index, GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES, uniformCount);

		int [] uniformTypes = getUniformBlockIndexParameter(p, GL_UNIFORM_TYPE, uniformIndices);
		int [] uniformSizes =  getUniformBlockIndexParameter(p, GL_UNIFORM_SIZE, uniformIndices);
		int [] uniformNameLengths = getUniformBlockIndexParameter(p, GL_UNIFORM_NAME_LENGTH, uniformIndices);
		int [] uniformOffsets = getUniformBlockIndexParameter(p, GL_UNIFORM_OFFSET, uniformIndices);
		int [] uniformArrayStrides = getUniformBlockIndexParameter(p, GL_UNIFORM_ARRAY_STRIDE, uniformIndices);
		int [] uniformMatrixStrides = getUniformBlockIndexParameter(p, GL_UNIFORM_MATRIX_STRIDE, uniformIndices);
		int [] uniformMatrixOrder = getUniformBlockIndexParameter(p, GL_UNIFORM_IS_ROW_MAJOR, uniformIndices);

		/* TODO complete UBO support */

		return at;
	}

	public static final Map<String, GLAttribute> getAttributeMap(int type, IntBuffer lengthBuf, IntBuffer sizeBuf, IntBuffer typeBuf, GLProgram p) {

		checkArgument(type == GL_ACTIVE_UNIFORMS || type == GL_ACTIVE_UNIFORM_BLOCKS || type == GL_ACTIVE_ATTRIBUTES);
		int objectCount = getGlslParam(p, type);
		Map<String, GLAttribute> programAttributes = new HashMap<String, GLAttribute>();
		GLAttribute at = null;

		for (int k = 0; k < objectCount; k++) {

			lengthBuf.clear(); sizeBuf.clear(); typeBuf.clear();

			switch (type) {
				case GL_ACTIVE_UNIFORMS: at = newUniformAttribute(k, lengthBuf, sizeBuf, typeBuf, p); break;
				case GL_ACTIVE_UNIFORM_BLOCKS: newUniformAttributeBlock(k, lengthBuf, sizeBuf, typeBuf, p); break;
				case GL_ACTIVE_ATTRIBUTES: at = newStageAttribute(k, lengthBuf, sizeBuf, typeBuf, p); break;
			}

			if (at != null) {
				if (log.isDebugEnabled()) { log.debug(at.toString()); }
				programAttributes.put(at.getName(), at);
			}
		}
		return programAttributes;
	}
}
