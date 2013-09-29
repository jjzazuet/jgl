package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.math.Preconditions.*;
import static net.tribe7.common.base.Preconditions.*;
import java.nio.ByteBuffer;
import net.tribe7.opengl.GLBuffer;

public class GLUniformBuffer {

	public static final int ZERO = 0;
	private GLBuffer dataBufffer;
	private final ByteBuffer backingBuffer;
	private final byte [] internalBuffer;

	public GLUniformBuffer(int size) {
		checkArgument(size > ZERO);
		this.internalBuffer = new byte [size];
		this.backingBuffer = ByteBuffer.wrap(internalBuffer);
	}

	public <T> void serialize(GLSerializableUniform<T> a, GLUniformBlockAttributeMetadata md, T ... data) {
		checkNoNulls(a, md, data);
		checkNoNulls(data);
		checkArgument(data.length > ZERO);
		a.serialize(getBackingBuffer(), md, data);
	}

	public byte[] getInternalBuffer() { return internalBuffer; }
	public ByteBuffer getBackingBuffer() { return backingBuffer; }
	public GLBuffer getDataBufffer() { return dataBufffer; }
	public void setDataBufffer(GLBuffer dataBufffer) {
		this.dataBufffer = checkNotNull(dataBufffer);
	}
}
