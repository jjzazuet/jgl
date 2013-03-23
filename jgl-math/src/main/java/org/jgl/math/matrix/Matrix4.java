package org.jgl.math.matrix;

import static java.lang.String.format;

public class Matrix4 {

	public static final int COMPONENT_SIZE = 16;
	public static final String ROW_FORMAT = "%n[%.6f, %.6f, %.6f, %.6f]";
	
	public double m00, m01, m02, m03;
	public double m10, m11, m12, m13;		
	public double m20, m21, m22, m23;		
	public double m30, m31, m32, m33;		

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(format(ROW_FORMAT, m00, m01, m02, m03));
		sb.append(format(ROW_FORMAT, m10, m11, m12, m13));
		sb.append(format(ROW_FORMAT, m20, m21, m22, m23));
		sb.append(format(ROW_FORMAT, m30, m31, m32, m33));
		
		return sb.toString();
	}
}
