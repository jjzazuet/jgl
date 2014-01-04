package net.tribe7.demos.mchochlik.t013SpiralSphere;

import static javax.media.opengl.GL.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.angle.AngleOps.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import javax.media.opengl.GL3;
import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.solid.Sphere;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.opengl.*;
import net.tribe7.opengl.camera.GLPointCamera;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/013_spiral_sphere.png")
public class T013SpiralSphere extends GL3EventListener {

	private Sphere sphere = new Sphere();
	private GLBuffer sphereIndices;
	private GLProgram p;
	private GLVertexArray sphereVao = new GLVertexArray();
	private ModelTransform modelTransform = new ModelTransform();
	private GLUFloatMat4 uCameraMatrix, uModelMatrix, uProjectionMatrix;
	private GLPointCamera camera = new GLPointCamera(70);

	@Override
	protected void doInit(GL3 gl) throws Exception {

		GLBuffer sphereVertices = buffer(sphere.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer sphereTexCoords = buffer(sphere.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		sphereIndices = buffer(sphere.getIndices(), gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
		
		p = loadProgram("/net/tribe7/demos/mchochlik/t013SpiralSphere/spiralSphere.vs", 
				"/net/tribe7/demos/mchochlik/t013SpiralSphere/spiralSphere.fs", gl);
		
		sphereVao.init(gl);
		p.bind();
		p.getInterface().getStageAttribute("Position").set(sphereVao, sphereVertices, false, 0).enable();
		p.getInterface().getStageAttribute("TexCoord").set(sphereVao, sphereTexCoords, false, 0).enable();

		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");
		uModelMatrix = p.getInterface().getMat4("ModelMatrix");
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
	
		camera.getPosition().set(2.5,  3.5,  2.5);
		gl.glClearColor(0.8f, 0.8f, 0.7f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		getDrawHelper().glClearColor().glClearDepth();
		sphereVao.bind();
		getDrawHelper().glIndexedDraw(GL_TRIANGLE_STRIP, sphereIndices);
		sphereVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double time = currentState.getElapsedTimeSeconds();

		lookAt(camera.getPosition(), camera.getTarget(), camera.getMatrix());
		uCameraMatrix.set(camera.getMatrix());
		modelTransform.getTranslation().setY(Math.sqrt(1 + sineWave(time / 2.0)));
		modelTransform.getRotationY().setDegrees(time * 180);
		uModelMatrix.set(modelTransform.getModelMatrix());
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(camera.getProjection(), camera.getFov(), 
				newViewport.width / newViewport.height, 1, 70);
		uProjectionMatrix.set(camera.getProjection());
	}
}