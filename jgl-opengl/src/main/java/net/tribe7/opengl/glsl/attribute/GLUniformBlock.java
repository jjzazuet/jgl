package net.tribe7.opengl.glsl.attribute;

import static javax.media.opengl.GL3.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.common.base.Preconditions.*;

import java.util.*;
import net.tribe7.opengl.GLBuffer;
import net.tribe7.opengl.glsl.*;

public class GLUniformBlock extends GLProgramVariable {

	private final int blockSize;
	private final Map<String, GLUniformBlockAttributeMetadata> blockMetadata = 
			new LinkedHashMap<String, GLUniformBlockAttributeMetadata>();
	private final GLUniformInterface Interface = new GLUniformInterface();

	public GLUniformBlock(int index, int blockSize, String name, GLProgram p) {
		super(index, name, p);
		checkArgument(blockSize > ZERO);
		this.blockSize = blockSize;
	}

	public void bindTo(GLUniformBuffer b) {

		checkNotNull(b);
		checkNotNull(b.getBackingBuffer());
		checkArgument(b.getInternalBuffer().length == getBlockSize());
		getProgram().checkBound();

		GLBuffer gb = b.getDataBufffer();

		if (gb == null) {
			gb = buffer(b.getInternalBuffer(), getProgram().getGl(), GL_UNIFORM_BUFFER, GL_DYNAMIC_DRAW, ONE);
			b.setDataBufffer(gb);
		}

		gb.bind();
		gb.getGl().glBindBufferBase(gb.getGlBufferType(), getIndex(), gb.getGlResourceHandle());
		gb.checkError();
		gb.unbind();
	}

	@Override
	public String toString() {
		return String.format("%s[name: %s, blockSize: %s, metadata: %s, uniforms: %s]", 
				getClass().getSimpleName(), getName(),
				getBlockSize(), getBlockMetadata(), getInterface());
	}

	public GLUniformBlockAttributeMetadata getBlockMetadata(String uniform) {
		checkNotNull(uniform);
		return checkNotNull(getBlockMetadata().get(uniform));
	}

	public void setBlockMetadata(String uniform, GLUniformBlockAttributeMetadata md) {
		checkNotNull(uniform);
		checkArgument(getInterface().getUniforms().containsKey(uniform));
		getBlockMetadata().put(uniform, md);
	}

	public int getBlockSize() { return blockSize; }
	public GLUniformInterface getInterface() { return Interface; }
	private Map<String, GLUniformBlockAttributeMetadata> getBlockMetadata() { return blockMetadata; }
}
