package org.jgl.geom.transform;

import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.matrix.Matrix4OpsGeom.*;

import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.Matrix4;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.Vector3;

public class ModelTransform {
	
	private BufferedMatrix4 modelMatrix = new BufferedMatrix4();
	
	private Matrix4 scaleMatrix = new Matrix4();
	private Matrix4 rotationMatrix = new Matrix4();
	private Matrix4 translationMatrix = new Matrix4();
	
	private Vector3 scaleVector = new Vector3(1, 1, 1);
	private Vector3 translationVector = new Vector3();
	
	private Angle rotationXAngle = new Angle();
	private Angle rotationYAngle = new Angle();
	private Angle rotationZAngle = new Angle();
		
	public void setScale(double x, double y, double z) {
		scaleVector.set(x, y, z);
	}
	
	public void setRotationX(double xDeg) {
		rotationXAngle.setDegrees(xDeg);
	}

	public void setRotationY(double yDeg) {
		rotationYAngle.setDegrees(yDeg);
	}

	public void setRotationZ(double zDeg) {
		rotationZAngle.setDegrees(zDeg);
	}

	public void setTranslation(double x, double y, double z) {
		translationVector.set(x, y, z);
	}
	
	public BufferedMatrix4 getModelMatrix() {
		
		setZero(modelMatrix);
		
		scaleXyz(scaleMatrix, scaleVector);
		rotateXLh(rotationMatrix, rotationXAngle);
		rotateYLh(rotationMatrix, rotationYAngle);
		rotateZLh(rotationMatrix, rotationZAngle);
		translateXyz(translationMatrix, translationVector);
		
		mul(scaleMatrix, rotationMatrix, modelMatrix);
		mul(translationMatrix, modelMatrix, modelMatrix);
		
		return modelMatrix;
	}
}
