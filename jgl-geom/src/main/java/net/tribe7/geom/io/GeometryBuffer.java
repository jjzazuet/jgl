package net.tribe7.geom.io;

import static net.tribe7.common.base.Preconditions.*;

public class GeometryBuffer<T extends Number> {

	public static final int ZERO = 0;
	public static final int MINUS_ONE = -1;
	private int componentSize = MINUS_ONE;
	private T[] data;

	public GeometryBuffer(int componentSize, T [] data) {
		checkArgument(componentSize > ZERO);
		this.componentSize = componentSize;
		setData(data);
	}

	public int getComponentSize() { return componentSize; }
	public T[] getData() {
		checkNotNull(data, "Geometry buffer is empty!");
		return data;
	}
	public void setData(T[] data) {
		this.data = checkNotNull(data);
		checkArgument(data.length > ZERO);
	}
}
