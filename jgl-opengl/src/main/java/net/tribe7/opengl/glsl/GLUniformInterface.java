package net.tribe7.opengl.glsl;

import static net.tribe7.common.base.Preconditions.checkNotNull;

import java.util.*;

import net.tribe7.opengl.glsl.attribute.*;

public class GLUniformInterface {

	private final Map<String, GLProgramVariable> uniforms = new HashMap<String, GLProgramVariable>();

	public GLUInt getInt(String name) {
		checkNameAccess(name);
		return (GLUInt) uniforms.get(name);
	}

	public GLUFloat getFloat(String name) {
		checkNameAccess(name);
		return (GLUFloat) uniforms.get(name);
	}

	public GLUFloatVec2 getVec2(String name) {
		checkNameAccess(name);
		return (GLUFloatVec2) uniforms.get(name);
	}

	public GLUFloatVec3 getVec3(String name) {
		checkNameAccess(name);
		return (GLUFloatVec3) uniforms.get(name);
	}

	public GLUFloatVec4 getVec4(String name) {
		checkNameAccess(name);
		return (GLUFloatVec4) uniforms.get(name);
	}

	public GLUFloatMat2 getMat2(String name) {
		checkNameAccess(name);
		return (GLUFloatMat2) uniforms.get(name);
	}

	public GLUFloatMat4 getMat4(String name) {
		checkNameAccess(name);
		return (GLUFloatMat4) uniforms.get(name);
	}

	public GLUSampler getSampler(String name) {
		checkNameAccess(name);
		return (GLUSampler) uniforms.get(name);
	}

	protected void checkNameAccess(String name) {
		checkNotNull(uniforms.get(name));
	}

	public Map<String, GLProgramVariable> getUniforms() { return uniforms; }

	@Override
	public String toString() {
		return String.format("%s[uniforms: %s]", getClass().getSimpleName(), getUniforms());
	}
}
