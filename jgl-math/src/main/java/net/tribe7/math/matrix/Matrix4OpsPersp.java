package net.tribe7.math.matrix;

import static net.tribe7.common.base.Preconditions.*;
import static java.lang.Math.*;
import static net.tribe7.math.matrix.Matrix4Ops.*;

import net.tribe7.math.angle.Angle;

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
	
	public static void perspectiveX(Matrix4 dst, Angle xFov, 
			double aspectRatio, double zNear, double zFar) {

		checkArgument(aspectRatio > 0);
		checkArgument(xFov.getRadians() > 0);
		
		double xRight = zNear * tan(xFov.getRadians() * .5);
		double xLeft = -xRight;
		double yBottom = xLeft / aspectRatio;
		double yTop = xRight / aspectRatio;
		
		perspectiveLh(dst, xLeft, xRight, yBottom, yTop, zNear, zFar);
	}
	
	public static void perspectiveY(Matrix4 dst, Angle yFov,
			double aspectRatio, double zNear, double zFar) {
		
		checkArgument(aspectRatio > 0);
		checkArgument(yFov.getRadians() > 0);
		
		double yTop = zNear * tan(yFov.getRadians() * .5);
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
