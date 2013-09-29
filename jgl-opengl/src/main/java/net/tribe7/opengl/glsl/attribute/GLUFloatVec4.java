package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import java.nio.ByteBuffer;
import net.tribe7.math.vector.Vector4;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUFloatVec4 extends GLSerializableUniform<Vector4> {

	public GLUFloatVec4(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y, double z, double w) {
		checkElementIndex(index, getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform4f(
				getIndexLocation(index), 
				(float) x, (float) y, (float) z, (float) w);
		getProgram().checkError();
	}

	public void set(double x, double y, double z, double w) {
		set(ZERO, x, y, z, w);
	}

	public void set(int index, float [] values) {
		checkNotNull(values);
		checkArgument(values.length == 4);
		set(index, values[0], values[1], values[2], values[3]);
	}

	@Override
	public void doSet(int index, Vector4 value) {
		set(index, value.x, value.y, value.z, value.w);
	}

	@Override
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, Vector4... data) {
		for (Vector4 v : data) {
			target.putFloat((float) v.x).putFloat((float) v.y).putFloat((float) v.z).putFloat((float) v.w);
			fillBytes(target, md.getArrayStride() - getUnitByteSize(), (byte) ONE);
		}
	}

	@Override
	public int getUnitByteSize() { return FOUR * FOUR; }
}
