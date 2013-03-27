package org.jgl.math.matrix;

import static java.lang.String.format;

/**
 * 4x4 matrix in column major order.
 * @author jjzazuet
 */
public class Matrix4 {

	public static final int COMPONENT_SIZE = 16;
	public static final String ROW_FORMAT = "%n[%.6f, %.6f, %.6f, %.6f]";
	
	public double m00, m10, m20, m30;
	public double m01, m11, m21, m31;		
	public double m02, m12, m22, m32;		
	public double m03, m13, m23, m33;		

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(format(ROW_FORMAT, m00, m10, m20, m30));
		sb.append(format(ROW_FORMAT, m01, m11, m21, m31));
		sb.append(format(ROW_FORMAT, m02, m12, m22, m32));
		sb.append(format(ROW_FORMAT, m03, m13, m23, m33));
		
		return sb.toString();
	}
}