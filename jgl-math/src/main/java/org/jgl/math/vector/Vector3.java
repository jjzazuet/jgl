package org.jgl.math.vector;

public class Vector3 extends Vector2 {
	
	public double z = 0;
	
	public Vector3() {}
	
	public Vector3(double x, double y , double z) {
		super(x, y); setZ(z);
	}
	
	public void setZ(double z) { this.z = z; }
	public void set(double x, double y, double z) { 
		setX(x); 
		setY(y); 
		setZ(z); 
	}
	
	public double get(int index) {
		if (index == 2) { return z; }
		return super.get(index);
	}
	
	public void set(int index, double value) {
		if (index == 2) { setZ(value); return; }
		super.set(index, value);
	}
	
	public double[] values() { return new double [] {x, y, z}; }
	public int length() { return 3; }
}
