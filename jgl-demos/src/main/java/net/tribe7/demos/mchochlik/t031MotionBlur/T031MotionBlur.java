package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.opengl.GLBufferFactory.*;
import static javax.media.opengl.GL3.*;

import javax.media.opengl.GL3;

import net.tribe7.geom.plane.Screen;
import net.tribe7.geom.solid.Cube;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.attribute.*;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

public class T031MotionBlur extends GL3EventListener {

	private final DrawProgram drawProg = new DrawProgram();
	private final BlurProgram blurProg = new BlurProgram();
	private final CheckerRedBlack checkerImage = new CheckerRedBlack(256, 256, 8, 8);
	private final GLTexture2D checkerTex = new GLTexture2D();
	private final MotionBlurBuffers mBuffers = new MotionBlurBuffers();
	private final MatrixInstances instances = new MatrixInstances(256);

	private final Cube cube = new Cube();
	private final GLVertexArray cubeVao = new GLVertexArray();
	private final GLVertexArray arrowVao = new GLVertexArray();

	private final Screen screen = new Screen();
	private final GLVertexArray screenVao = new GLVertexArray();

	@Override
	protected void doInit(GL3 gl) throws Exception {

		initResource(gl, drawProg, blurProg, mBuffers);
		GLUniformBlock ub = drawProg.getModelBlock();
		GLUFloatMat4 modelMatrices = ub.getInterface().getMat4("ModelMatrices");
		GLUniformBuffer b = new GLUniformBuffer(ub.getBlockSize());

		initResource(gl, screenVao, cubeVao, arrowVao, checkerTex);

		checkerTex.bind(); {
			checkerTex.getImage().setFrom(checkerImage);
			checkerTex.loadData();
			checkerTex.generateMipMap();
			checkerTex.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			checkerTex.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			checkerTex.getParameters().put(GL_TEXTURE_WRAP_S, GL_REPEAT);
			checkerTex.getParameters().put(GL_TEXTURE_WRAP_T, GL_REPEAT);
			checkerTex.applyParameters();
		} checkerTex.unbind();

		mBuffers.bind(); {
			mBuffers.initAttachments();
		} mBuffers.unbind();

		GLBuffer cubeVertices = buffer(cube.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeNormals = buffer(cube.getNormals(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer cubeTexCoords = buffer(cube.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		drawProg.bind(); {
			drawProg.getPosition().set(cubeVao, cubeVertices, false, 0).enable();
			drawProg.getNormal().set(cubeVao, cubeNormals, false, 0).enable();
			drawProg.getTexCoord().set(cubeVao, cubeTexCoords, false, 0).enable();
			drawProg.getPosition().set(arrowVao, cubeVertices, false, 0).enable();
			drawProg.getNormal().set(arrowVao, cubeNormals, false, 0).enable();
			drawProg.getCheckerTex().set(checkerTex);
			b.serialize(
					modelMatrices,
					ub.getBlockMetadata("ModelMatrices"),
					instances.getMatrixData().toArray(new BufferedMatrix4 []{}));
			ub.bindTo(b, GL_STATIC_DRAW);
			// drawProg.getModelBlock(). TODO determine how to bind the uniform buffer to the shader block index
		} drawProg.unbind();

		GLBuffer screenVertices = buffer(screen.getVertices(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
		GLBuffer screenTexCoords = buffer(screen.getTexCoords(), gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW);

		blurProg.bind(); {
			blurProg.getPosition().set(screenVao, screenVertices, false, 0).enable();
			blurProg.getTexCoord().set(screenVao, screenTexCoords, false, 0).enable();
			blurProg.getCurrentFrame().set(mBuffers.getColor0());
			blurProg.getPreviousFrames().set(mBuffers.getColor1());
		} blurProg.unbind();

		gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
		gl.glClearDepth(1.0f);
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
