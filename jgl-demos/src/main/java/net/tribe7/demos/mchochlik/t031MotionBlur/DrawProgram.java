package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.common.base.Throwables.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.GLShader;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.glsl.attribute.GLUInt;
import net.tribe7.opengl.glsl.attribute.GLUSampler;
import net.tribe7.opengl.glsl.attribute.GLUniformBlock;
import net.tribe7.opengl.glsl.attribute.GLVertexAttribute;

public class DrawProgram extends GLProgram {

	private GLVertexAttribute position, normal, texCoord;
	private GLUFloatMat4 projectionMatrix, cameraMatrix, modelMatrix;
	private GLUInt singleModel;
	private GLUSampler checkerTex;
	private GLUniformBlock modelBlock;

	@Override
	protected int doInit() { // TODO make something more convenient :S. e.g. a File based GLProgram.
		try {
			GLShader drawVs = loadVertexShader("/net/tribe7/demos/mchochlik/t031MotionBlur/draw.vs");
			GLShader drawFs = loadFragmentShader("/net/tribe7/demos/mchochlik/t031MotionBlur/draw.fs");
			attachShader(drawVs).attachShader(drawFs);
		} catch (Exception e) { propagate(e); }

		int rh = super.doInit();
		position = getInterface().getStageAttribute("Position");
		normal = getInterface().getStageAttribute("Normal");
		texCoord = getInterface().getStageAttribute("TexCoord");
		projectionMatrix = getInterface().getMat4("ProjectionMatrix");
		cameraMatrix = getInterface().getMat4("CameraMatrix");
		modelMatrix = getInterface().getMat4("SingleModelMatrix");
		singleModel = getInterface().getInt("SingleModel");
		checkerTex = getInterface().getSampler("CheckerTex");
		modelBlock = getInterface().getUniformBlock("ModelBlock");
		return rh;
	}

	public GLVertexAttribute getPosition() { return position; }
	public GLVertexAttribute getNormal() { return normal; }
	public GLVertexAttribute getTexCoord() { return texCoord; }

	public GLUFloatMat4 getProjectionMatrix() { return projectionMatrix; }
	public GLUFloatMat4 getCameraMatrix() { return cameraMatrix; }
	public GLUFloatMat4 getModelMatrix() { return modelMatrix; }

	public GLUInt getSingleModel() { return singleModel; }
	public GLUSampler getCheckerTex() { return checkerTex; }
	public GLUniformBlock getModelBlock() { return modelBlock; }
}
