package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.opengl.GLBufferFactory.*;
import static javax.media.opengl.GL3.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.angle.AngleOps.*;

import javax.media.opengl.GL3;
import net.tribe7.geom.plane.Screen;
import net.tribe7.geom.solid.Cube;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.opengl.*;
import net.tribe7.opengl.camera.GLPointCamera;
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
	private final Screen screen = new Screen();
	private final GLPointCamera camera = new GLPointCamera(60);
	private final GLVertexArray cubeVao = new GLVertexArray();
	private final GLVertexArray arrowVao = new GLVertexArray();
	private final GLVertexArray screenVao = new GLVertexArray();

	private GLViewSize currentSize;

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

		mBuffers.bind();
		mBuffers.initAttachments();
		mBuffers.unbind();

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

		int samples = 8;
		double frame_time = currentState.getElapsedTimeSeconds();
		double interval = currentState.getRenderTimer().getElapsedSeconds();
		double step = interval/samples;

		for(int s=0; s!=samples; ++s) {

			double time = frame_time + s*step;
			
			mBuffers.bind(); {

				gl.glEnable(GL_DEPTH_TEST);
				getDrawHelper().glClearColor().glClearDepth();

				double pos = time/20.0;

				drawProg.bind(); {

					lookAt(instances.position(pos-0.03+sineWave(time/7.0)*0.01),
							instances.position(pos+0.02+sineWave(time/11.0)*0.01),
							instances.normal(pos-0.02+sineWave(time/9.0)*0.02),
							camera.getMatrix());

					drawProg.getCameraMatrix().set(camera.getMatrix());
					drawProg.getSingleModel().set(0);

					cubeVao.bind(); { // TODO abstract rendering code.
						gl.glFrontFace(GL_CW);
						gl.glDrawArraysInstanced(GL_TRIANGLES, 0, 36, 512);
					} cubeVao.unbind();

					drawProg.getSingleModel().set(1);

					arrowVao.bind(); {
						BufferedMatrix4 bm1 = instances.makeMatrix(pos-0.007+sineWave(time/13.0)*0.007, 0.001);
						drawProg.getModelMatrix().set(bm1);
						// arrow.Draw();
						BufferedMatrix4 bm2 = instances.makeMatrix(pos+0.013+sineWave(time / 7.0)*0.007, 0.001);
						drawProg.getModelMatrix().set(bm2);
						// arrow.Draw();
					} arrowVao.unbind();
				} drawProg.unbind();
			} mBuffers.unbind();

			// motion blur
			gl.glDisable(GL_DEPTH_TEST);

			blurProg.bind(); {
				screenVao.bind(); {
					// screen.Draw();
				} screenVao.unbind();
				mBuffers.accumulate();
			} blurProg.unbind();
		}

		double splitter = (sineWave(frame_time / 20.0)*0.5+0.5) * currentSize.width;
		blurProg.bind();
		blurProg.getSplitter().set((float) splitter);
		blurProg.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		currentSize = newViewport;
		getDrawHelper().glViewPort((int) newViewport.width, (int) newViewport.height);
		perspectiveX(camera.getProjection(), camera.getFov(), newViewport.aspectRatio, 1, 300);
		drawProg.bind();
		drawProg.getProjectionMatrix().set(camera.getProjection());
		drawProg.unbind();
		blurProg.bind();
		blurProg.getScreenSize().set(newViewport.width, newViewport.height);
		blurProg.unbind();
	}
}
