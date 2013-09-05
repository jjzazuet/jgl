package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.checkNotNull;
import net.tribe7.opengl.glsl.GLProgram;

public class GLProgramVariable {

	public static final int ZERO = 0;
	public static final int ONE = 1;

	private final int index;
	private final String name;
	private final GLProgram program;

	public GLProgramVariable(int index, String name, GLProgram p) {
		this.program = checkNotNull(p);
		this.name = checkNotNull(name);
		this.index = index;
	}

	public int getIndex() { return index; }
	public String getName() { return name; }
	public GLProgram getProgram() { return program; }
}
