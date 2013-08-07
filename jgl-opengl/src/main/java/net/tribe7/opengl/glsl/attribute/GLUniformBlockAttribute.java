package net.tribe7.opengl.glsl.attribute;

public class GLUniformBlockAttribute {

	private final int offset;
	private final int arrayStride;
	private final int matrixStride;
	private final int matrixOrder;
	private final GLUniformAttribute<?> target;

	public GLUniformBlockAttribute(int offset, int arrayStride, int matrixStride, int matrixOrder, GLUniformAttribute<?> target) {
		this.offset = offset;
		this.arrayStride = arrayStride;
		this.matrixStride = matrixStride;
		this.matrixOrder = matrixOrder;
		this.target = target;
	}

	@Override
	public String toString() {
		return String.format("%s[offset: %s, arrayStride: %s, matrixStride: %s, matrixOrder: %s, target: %s]", 
				getClass().getSimpleName(),
				getOffset(), getArrayStride(), getMatrixStride(), getMatrixOrder(), getTarget());
	}

	public int getOffset() { return offset; }
	public int getArrayStride() { return arrayStride; }
	public int getMatrixStride() { return matrixStride; }
	public int getMatrixOrder() { return matrixOrder; }
	public GLUniformAttribute<?> getTarget() { return target; }
}
