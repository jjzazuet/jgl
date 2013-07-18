package net.tribe7.demos.mchochlik;

import static net.tribe7.opengl.util.GLSLUtils.*;
import javax.media.opengl.GL3;

import net.tribe7.opengl.GL3EventListener;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.GLShader;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T030CubeMapping extends GL3EventListener {

	private GLProgram sphereProg = new GLProgram(), cubeProg = new GLProgram(), cubeMapProg = new GLProgram();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		GLShader sphereVs = loadVertexShader("/net/tribe7/demos/mchochlik/t030CubeMapping/sphere.vs");
		GLShader sphereFs = loadFragmentShader("/net/tribe7/demos/mchochlik/t030CubeMapping/sphere.fs");
		GLShader cubeVs = loadVertexShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cube.vs");
		GLShader cubeFs = loadFragmentShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cube.fs");
		GLShader cubeMapVs = loadVertexShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cubeMap.vs");
		GLShader cubeMapGs = loadGeometryShader("/net/tribe7/demos/mchochlik/t030CubeMapping/cubeMap.gs");

		cubeProg.attachShader(cubeVs).attachShader(cubeFs).init(gl);
		cubeMapProg.attachShader(cubeMapVs).attachShader(cubeMapGs).attachShader(cubeFs).init(gl);
		sphereProg.attachShader(sphereVs).attachShader(sphereFs).init(gl);
		
		log.info("");
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		// TODO Auto-generated method stub

	}

}
