package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import java.nio.ByteBuffer;
import java.util.*;
import net.tribe7.opengl.glsl.GLUniformInterface;

public class GLUniformBlock extends GLProgramVariable {

	private final int blockSize;
	private final Map<String, GLUniformBlockAttributeMetadata> blockMetadata = 
			new LinkedHashMap<String, GLUniformBlockAttributeMetadata>();
	private final GLUniformInterface Interface = new GLUniformInterface();
	private final ByteBuffer backingBuffer;

	public GLUniformBlock(int index, int blockSize, String name) {
		super(index, name);
		checkArgument(blockSize > ZERO);
		this.blockSize = blockSize;
		this.backingBuffer = ByteBuffer.allocateDirect(blockSize);
	}

	public <T> void serialize(GLUniformAttribute<T> a, T ... data) {
		checkNotNull(a);
		checkNotNull(data);
		checkArgument(data.length > ZERO);
		a.serialize(getBackingBuffer(), checkNotNull(getBlockMetadata().get(a.getName())), data);
	}

	@Override
	public String toString() {
		return String.format("%s[name: %s, blockSize: %s, buffer: %s, metadata: %s, uniforms: %s]", 
				getClass().getSimpleName(), getName(), 
				getBlockSize(), getBackingBuffer(),
				getBlockMetadata(), getInterface());
	}

	public int getBlockSize() { return blockSize; }
	public ByteBuffer getBackingBuffer() { return backingBuffer; }
	public GLUniformInterface getInterface() { return Interface; }
	public Map<String, GLUniformBlockAttributeMetadata> getBlockMetadata() { return blockMetadata; }
}
