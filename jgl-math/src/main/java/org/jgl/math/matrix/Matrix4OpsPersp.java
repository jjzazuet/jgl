package org.jgl.math.matrix;

import static com.google.common.base.Preconditions.*;
import static java.lang.Math.*;
import static org.jgl.math.matrix.Matrix4Ops.*;

public class Matrix4OpsPersp {

	public static void perspectiveLh(Matrix4 dst, 
			double xLeft, double xRight, 
			double yBottom, double yTop, 
			double zNear, double zFar) {
		
		checkNotNull(dst);
		setZero(dst);
		
		dst.m00 =  (2 * zNear) / (xRight - xLeft);
		dst.m11 =  (2 * zNear) / (yTop - yBottom);
		
		dst.m20 =  (xRight + xLeft) / (xRight - xLeft);
		dst.m21 =  (yTop + yBottom) / (yTop - yBottom);
		dst.m22 = -(zFar + zNear) / (zFar - zNear);
		dst.m23 = -1;
		
		dst.m32 = -(2 * zFar * zNear) / (zFar - zNear);
	}
	
	public static void perspectiveX(Matrix4 dst, double xFov, 
			double aspectRatio, double zNear, double zFar) {

		checkArgument(aspectRatio > 0);
		checkArgument(toRadians(xFov) > 0);
		xFov = toRadians(xFov);
		
		double xRight = zNear * tan(xFov * .5);
		double xLeft = -xRight;
		double yBottom = xLeft / aspectRatio;
		double yTop = xRight / aspectRatio;
		
		perspectiveLh(dst, xLeft, xRight, yBottom, yTop, zNear, zFar);
	}
	
	public static void perspectiveY(Matrix4 dst, double yFov,
			double aspectRatio, double zNear, double zFar) {
		
		checkArgument(aspectRatio > 0);
		checkArgument(toRadians(yFov) > 0);
		yFov = toRadians(yFov);
		
		double yTop = zNear * tan(yFov * .5);
		double yBottom = -yTop;
		double xLeft = yBottom * aspectRatio;
		double xRight = yTop * aspectRatio;
		
		perspectiveLh(dst, xLeft, xRight, yBottom, yTop, zNear, zFar);
	}
	
	public static void orthoLh(Matrix4 dst, 
			double xLeft, double xRight, 
			double yBottom, double yTop, 
			double zNear, double zFar) {
	
		checkNotNull(dst);
		setIdentity(dst);
		
		dst.m00 =  2 / (xRight - xLeft);
		dst.m11 =  2 / (yTop - yBottom);
		dst.m22 = -2 / (zFar - zNear);
		
		dst.m30 = -(xRight + xLeft) / (xRight - xLeft);
		dst.m31 = -(yTop + yBottom) / (yTop - yBottom);
		dst.m32 = -(zFar + zNear) / (zFar - zNear);
	}
	
	public static void orthoX(Matrix4 dst, double width, 
			double aspectRatio, double zNear, double zFar) {
		
		checkArgument(aspectRatio > 0);
		checkArgument(width > 0);
		
		double xRight = width / 2;
		double xLeft = -xRight;
		double yBottom = xLeft / aspectRatio;
		double yTop = xRight / aspectRatio;
		
		orthoLh(dst, xLeft, xRight, yBottom, yTop, zNear, zFar);
	}
	
	public static void orthoY(Matrix4 dst, double height, 
			double aspectRatio, double zNear, double zFar) {
		
		checkArgument(aspectRatio > 0);
		checkArgument(height > 0);
		
		double yTop = height / 2;
		double yBottom = -yTop;
		double xLeft = yBottom * aspectRatio;
		double xRight = yTop * aspectRatio;
		
		orthoLh(dst, xLeft, xRight, yBottom, yTop, zNear, zFar);
	}
}
