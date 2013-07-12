package net.tribe7.math.vector;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

public class VectorOps {
	
	public static void add(Vector l, Vector r, Vector dst) {
		checkDimensions(l, r, dst);
		for (int k = 0; k < l.length(); k++) {
			dst.set(k, l.get(k) + r.get(k));
		}
	}

	public static void sub(Vector l, Vector r, Vector dst) {
		checkDimensions(l, r, dst);
		for (int k = 0; k < l.length(); k++) {
			dst.set(k, l.get(k) - r.get(k));
		}
	}
		
	public static void scale(Vector src, Vector dst, double scale) {
		checkDimensions(src, dst);
		for (int k = 0; k < src.length(); k++) {
			dst.set(k, src.get(k) * scale);
		}
	}
	
	public static void scale(Vector src, double scale) {
		scale(src, src, scale);
	}
	
	public static double dot(Vector l, Vector r) {
		checkDimensions(l, r);
		double dot = 0;
		for (int k = 0; k < l.length(); k++) {
			dot += (l.get(k) * r.get(k));
		}
		return dot;
	}

	public static void negate(Vector src) { negate(src, src); }

	public static void negate(Vector src, Vector dst) {
		checkDimensions(src, dst);
		double [] srcValues = src.values();
		for (int k = 0; k < srcValues.length; k++) { dst.set(k, -srcValues[k]); }
	}
	
	public static double length(Vector src) {
		
		checkNotNull(src);
		double vLength = 0;
		
		for (int k = 0; k < src.length(); k++) {
			double val = src.get(k);
			vLength += (val * val);
		}
		
		return Math.sqrt(vLength);
	}
	
	public static double distance(Vector a, Vector b) throws Exception {
		checkDimensions(a, b);
		Vector sub = a.getClass().newInstance();
		sub(a, b, sub);
		return length(sub);
	}
	
	public static void normalize(Vector src, Vector dst) {
		
		checkDimensions(src, dst);
		double vLength = length(src);
		
		for (int k = 0; k < src.length(); k++) {
			dst.set(k, src.get(k) / vLength);
		}
	}
	
	public static void normalize(Vector src) {
		normalize(src, src);
	}
	
	public static void checkDimensions(Vector ... values) {

		checkNoNulls(values);
		checkArgument(values.length != 0, "No vectors provided: %s", (Object []) values);
		
		if (values.length > 1) {
		
			int dim0 = values[0].length();
			
			for (int k = 1; k < values.length; k++) {
				checkArgument(
						values[k].length() == dim0, 
						"Vector does not have the same length of previous elements: %s", 
						values[k]);
			}
		}		
	}
}
