package net.tribe7.math.vector;

public class Vector2 extends Vector {

	public double y = 0;

	public Vector2() {}
	public Vector2(double x, double y) {
		super(x); setY(y);
	}
	
	public void setY(double y) { this.y = y; }
	public void set(double x, double y) { 
		setX(x); 
		setY(y); 
	}
	
	public double get(int index) {
		if (index == 1) { return y; }
		return super.get(index);
	}
	
	public void set(int index, double value) {
		if (index == 1) { setY(value); return; }
		super.set(index, value);
	}
	
	public double[] values() { return new double [] {x, y}; }
	public int length() { return 2; }
}
