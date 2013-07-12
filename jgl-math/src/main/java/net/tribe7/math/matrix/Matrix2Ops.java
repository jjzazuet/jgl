package net.tribe7.math.matrix;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.checkNoNulls;

import java.nio.FloatBuffer;

public class Matrix2Ops {

	public static void storeColMaj(FloatBuffer b, Matrix2 ... src) {
		
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
	
	public static void setIdentity(Matrix2 m) {
		checkNotNull(m);
		m.m00 = 1; m.m10 = 0;
		m.m01 = 0; m.m11 = 1;
	}
}
