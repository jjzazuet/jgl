package net.tribe7.math.vector;

public class Vector4 extends Vector3 {

	public double w = 0;

	public Vector4() {}
	public Vector4(double x, double y, double z, double w) {
		super(x, y, z); setW(w);
	}
	
	public void setW(double w) { this.w = w; }
	public void set(double x, double y, double z, double w) { 
		setX(x); 
		setY(y); 
		setZ(z); 
		setW(w); 
	}
	
	public double get(int index) {
		if (index == 3) { return w; }
		return super.get(index);
	}
	
	public void set(int index, double value) {
		if (index == 3) { setW(value); return; }
		super.set(index, value);
	}
	
	public double[] values() { return new double [] {x, y, z, w}; }
	public int length() { return 4; }
}
