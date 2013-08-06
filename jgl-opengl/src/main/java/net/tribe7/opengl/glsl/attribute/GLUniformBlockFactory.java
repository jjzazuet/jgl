package net.tribe7.opengl.glsl.attribute;

import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.glsl.attribute.GLAttributeBuffers.*;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.*;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUniformBlockFactory {

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

	protected static GLAttribute newUniformAttributeBlock(int index, GLProgram p) {

		GLAttribute at = null;
		GLAttributeBuffers ab = new GLAttributeBuffers(index, getUniformBlockParameter(p, index, GL_UNIFORM_BLOCK_NAME_LENGTH, ONE)[0]);

		p.getGl().glGetActiveUniformBlockName(p.getGlResourceHandle(), 
				ab.getAttributeIndex(), ab.getName().limit(), ab.getLength(), ab.getName());
		p.checkError();

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
}
