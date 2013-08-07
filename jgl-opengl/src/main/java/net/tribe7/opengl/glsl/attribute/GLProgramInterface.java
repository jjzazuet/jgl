package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.checkNotNull;

public class GLProgramInterface {

	public static final int ZERO = 0;

	private final int index;
	private final String name;

	public GLProgramInterface(int index, String name) {
		this.name = checkNotNull(name);
		this.index = index;
	}

	public int getIndex() { return index; }
	public String getName() { return name; }
}
