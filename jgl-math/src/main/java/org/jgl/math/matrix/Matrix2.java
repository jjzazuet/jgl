package org.jgl.math.matrix;

import static java.lang.String.format;

public class Matrix2 {

	public static final int COMPONENT_SIZE = 4;
	public static final String ROW_FORMAT = "%n[%.6f, %.6f]";
	
	public double m00, m01;
	public double m10, m11;		

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(format(ROW_FORMAT, m00, m01));
		sb.append(format(ROW_FORMAT, m10, m11));		
		return sb.toString();
	}
}
