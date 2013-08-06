package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.common.base.Throwables.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.GLShader;

public class DrawProgram extends GLProgram {

	@Override
	protected void doInit() {
		try {
			GLShader drawVs = loadVertexShader("/net/tribe7/demos/mchochlik/t031MotionBlur/draw.vs");
			GLShader drawFs = loadFragmentShader("/net/tribe7/demos/mchochlik/t031MotionBlur/draw.fs");
			attachShader(drawVs).attachShader(drawFs);
		} catch (Exception e) { propagate(e); }
		super.doInit();
	}
}
