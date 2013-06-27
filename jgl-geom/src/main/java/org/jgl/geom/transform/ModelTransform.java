package org.jgl.geom.transform;

import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.matrix.Matrix4OpsGeom.*;
import static org.jgl.math.vector.VectorOps.*;
import static org.jgl.math.quaternion.QuaternionOps.*;

import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.Matrix4;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.*;

public class ModelTransform {
	
	private BufferedMatrix4 modelMatrix = new BufferedMatrix4();
	
	private final Matrix4 scaleMatrix = new Matrix4();
	private final Matrix4 rotationMatrix = new Matrix4();
	private final Matrix4 translationMatrix = new Matrix4();
	
	private final Vector3 scale = new Vector3(1, 1, 1);
	private final Vector3 translation = new Vector3();
	
	private final Vector4 orientX = new Vector4();
	private final Vector4 orientY = new Vector4();	
	private final Vector4 orientZ = new Vector4();
	
	private final Angle rotationX = new Angle();
	private final Angle rotationY = new Angle();
	private final Angle rotationZ = new Angle();
	
	public BufferedMatrix4 getModelMatrix() {

		setZero(modelMatrix);
		scaleXyz(scaleMatrix, scale);

		orientX.set(1, 0, 0, 0);
		orientY.set(0, 1, 0, 0);
		orientZ.set(0, 0, 1, 0);

		setQuaternion(orientX, rotationX);
		setQuaternion(orientY, rotationY);
		setQuaternion(orientZ, rotationZ);

		mulQuat(orientZ, orientY, orientY); 
		mulQuat(orientY, orientX, orientX); 
		normalize(orientX);
		fromQuaternion(rotationMatrix, orientX);

		translateXyz(translationMatrix, translation);

		mul(scaleMatrix, rotationMatrix, modelMatrix);
		mul(translationMatrix, modelMatrix, modelMatrix);

		return modelMatrix;
	}
	
	public void setScale(double s) {
		setScale(s, s, s);
	}
	
	public void setScale(double x, double y, double z) {
		scale.set(x, y, z);
	}
	
	public Angle getRotationX() { return rotationX; }
	public Angle getRotationY() { return rotationY; }
	public Angle getRotationZ() { return rotationZ; }
	
	public Vector3 getScale() { return scale; }
	public Vector3 getTranslation() { return translation; }
}
