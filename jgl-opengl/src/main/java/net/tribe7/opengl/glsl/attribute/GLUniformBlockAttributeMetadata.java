package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;

public class GLUniformBlockAttributeMetadata {

	public static final int UNIFORM_BUFFER_COLUMN_MAJOR = 0;
	public static final int UNIFORM_BUFFER_ROW_MAJOR = 1;

	private final String name;
	private final int offset;
	private final int arrayStride;
	private final int matrixStride;
	private final int matrixOrder;

	public GLUniformBlockAttributeMetadata(int offset, int arrayStride, int matrixStride, int matrixOrder, String name) {
		this.offset = offset;
		this.arrayStride = arrayStride;
		this.matrixStride = matrixStride;
		this.matrixOrder = matrixOrder;
		this.name = checkNotNull(name);
	}

	@Override
	public String toString() {
		return String.format("%s[name: %s, offset: %s, arrayStride: %s, matrixStride: %s, matrixOrder: %s]", 
				getClass().getSimpleName(), getName(),
				getOffset(), getArrayStride(), 
				getMatrixStride(), getMatrixOrder());
	}

	public String getName() { return name; }
	public int getOffset() { return offset; }
	public int getArrayStride() { return arrayStride; }
	public int getMatrixStride() { return matrixStride; }
	public int getMatrixOrder() { return matrixOrder; }
}
