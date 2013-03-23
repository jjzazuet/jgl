package org.jgl.math.matrix;

import static com.google.common.base.Preconditions.*;
import static org.jgl.math.Preconditions.checkNoNulls;

import java.nio.FloatBuffer;

public class Matrix2Ops {

	public static void store(FloatBuffer b, Matrix2 ... src) {
		
		checkNoNulls(src);
		checkNotNull(b);
		checkArgument(src.length >= 1);
		checkArgument(b.capacity() >= src.length * Matrix2.COMPONENT_SIZE);

		b.clear();
		
		for (Matrix2 m : src) {
			b.put((float) m.m00); b.put((float) m.m01);
			b.put((float) m.m10); b.put((float) m.m11);
		}
		
		b.flip();
	}
}
