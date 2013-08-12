package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import java.nio.ByteBuffer;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUFloatVec3 extends GLUniformAttribute<Vector3> {

	public GLUFloatVec3(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y, double z) {
		checkElementIndex(index, getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform3f(getIndexLocation(index), (float) x, (float) y, (float) z);
		getProgram().checkError();
	}

	public void set(double x, double y, double z) {
		set(ZERO, x, y, z);
	}

	@Override
	public void doSet(int index, Vector3 value) {
		set(index, value.x, value.y, value.z);
	}

	@Override
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, Vector3... data) {
		for (Vector3 v : data) {
			target.putFloat((float) v.x).putFloat((float) v.y).putFloat((float) v.z);
			fillBytes(target, md.getArrayStride() - getUnitByteSize(), (byte) ONE);
		}
	}

	@Override
	public int getUnitByteSize() { return FOUR * THREE; }
}
