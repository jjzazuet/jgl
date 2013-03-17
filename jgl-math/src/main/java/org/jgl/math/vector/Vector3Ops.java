package org.jgl.math.vector;

import static org.jgl.math.Preconditions.*;
import static java.lang.Math.*;
import static org.jgl.math.vector.Vector4Ops.*;

public class Vector3Ops {

	public static void cross(Vector3 l, Vector3 r, Vector3 dst) {
		
		checkNoNulls(l, r, dst);

		double cx = l.y * r.z - l.z * r.y;
		double cy = l.z * r.x - l.x * r.z;
		double cz = l.x * r.y - l.y * r.x;
		
		dst.set(cx, cy, cz);
	}

	public static void rotate(Vector3 src, Vector3 dst, Vector3 rotationAxes, double angleDeg) {
		
		checkNoNulls(src, dst, rotationAxes);
		
		double sinHalfAngle = sin(toRadians(angleDeg/2));
		double cosHalfAngle = cos(toRadians(angleDeg/2));

		double Rx = rotationAxes.x * sinHalfAngle;
		double Ry = rotationAxes.y * sinHalfAngle;
	    double Rz = rotationAxes.z * sinHalfAngle;
	    double Rw = cosHalfAngle;
	    
	    Vector4 rotationQ = new Vector4(Rx, Ry, Rz, Rw);
	    Vector4 conjugateQ = new Vector4();

	    conjugateQuaternion(rotationQ, conjugateQ);

	    Vector4 W = new Vector4();
	    
	    mulVec(rotationQ, src, W);
	    mulQuat(W, conjugateQ, W);
	    	    
	    dst.set(W.x, W.y, W.z);
	}
}
