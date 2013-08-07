package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;

import java.util.*;

public class GLUniformBlock extends GLProgramInterface {

	private final int blockSize;
	private final Map<String, GLUniformBlockAttribute> attributes = new LinkedHashMap<String, GLUniformBlockAttribute>();

	public GLUniformBlock(int index, int blockSize, String name) {
		super(index, name);
		checkArgument(blockSize > ZERO);
		this.blockSize = blockSize;
	}

	public GLUniformBlockAttribute getAttribute(String name) {
		checkNotNull(name);
		return checkNotNull(getAttributes().get(name));
	}

	@Override
	public String toString() {
		return String.format("%s[name: %s, blockSize: %s, attributes: %s]", 
				getClass().getSimpleName(), getName(), getBlockSize(), getAttributes());
	}

	public int getBlockSize() { return blockSize; }
	public Map<String, GLUniformBlockAttribute> getAttributes() { return attributes; }
}
