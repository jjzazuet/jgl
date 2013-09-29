package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;
import static net.tribe7.opengl.glsl.attribute.GLUniformBlockAttributeMetadata.*;

import java.nio.ByteBuffer;

import net.tribe7.opengl.glsl.GLProgram;

public abstract class GLSerializableUniform<T> extends GLUniformAttribute<T> {

	public GLSerializableUniform(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	protected abstract void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, T ... data);
	public void serialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, T ... data) {
		checkNoNulls(target, md);
		checkNoNulls(data);
		checkArgument(data.length >= ONE);
		checkArgument(data.length <= getSize());
		checkArgument(md.getMatrixOrder() == UNIFORM_BUFFER_COLUMN_MAJOR || md.getMatrixOrder() == UNIFORM_BUFFER_ROW_MAJOR);
		target.position(md.getOffset());
		checkArgument(target.remaining() >= getUnitByteSize());
		doSerialize(target, md, data);
	}

	protected void fillBytes(ByteBuffer b, int howMany, byte value) {
		checkNotNull(b);
		checkArgument(b.remaining() >= howMany);
		for (int i = 0; i < howMany; i++) {
			b.put(value);
		}
	}
}
