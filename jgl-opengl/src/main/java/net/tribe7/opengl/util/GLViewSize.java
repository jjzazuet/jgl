package net.tribe7.opengl.util;

public class GLViewSize {
	
	public int x, y;
	public double width, height, aspectRatio;
	
	public GLViewSize(int x, int y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.aspectRatio = this.width / this.height;
	}
	
	@Override
	public String toString() {
		return String.format(
				"%s[x:%s, y:%s, w:%s, h:%s]", 
				getClass().getSimpleName(),
				x, y, width, height);
	}
}
