package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.common.base.Throwables.propagate;
import static net.tribe7.opengl.util.GLSLUtils.*;
import net.tribe7.opengl.glsl.*;
import net.tribe7.opengl.glsl.attribute.*;

public class BlurProgram extends GLProgram {

	private GLVertexAttribute position, texCoord;
	private GLUFloatVec2 screenSize;
	private GLUSampler currentFrame, previousFrames;
	private GLUFloat splitter;

	@Override
	protected void doInit() {
		try {
			GLShader drawVs = loadVertexShader("/net/tribe7/demos/mchochlik/t031MotionBlur/blur.vs");
			GLShader drawFs = loadFragmentShader("/net/tribe7/demos/mchochlik/t031MotionBlur/blur.fs");
			attachShader(drawVs).attachShader(drawFs);
		} catch (Exception e) { propagate(e); }
		super.doInit();
		position = getInterface().getStageAttribute("Position");
		texCoord = getInterface().getStageAttribute("TexCoord");
		screenSize = getInterface().getVec2("ScreenSize");
		currentFrame = getInterface().getSampler("CurrentFrame");
		previousFrames = getInterface().getSampler("PreviousFrames");
		splitter = getInterface().getFloat("Splitter");
	}

	public GLVertexAttribute getPosition() { return position; }
	public GLVertexAttribute getTexCoord() { return texCoord; }

	public GLUFloatVec2 getScreenSize() { return screenSize; }
	public GLUSampler getCurrentFrame() { return currentFrame; }
	public GLUSampler getPreviousFrames() { return previousFrames; }
	public GLUFloat getSplitter() { return splitter; }
}
