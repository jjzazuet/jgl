package net.tribe7.demos.mchochlik.t015Graph;

import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL3.*;
import static net.tribe7.math.matrix.Matrix4OpsCam.*;
import static net.tribe7.math.matrix.Matrix4OpsPersp.perspectiveX;
import static net.tribe7.math.vector.VectorOps.*;
import static net.tribe7.opengl.GLBufferFactory.*;
import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.opengl.util.GLSLUtils.*;

import java.util.LinkedList;

import javax.media.opengl.GL3;

import net.tribe7.demos.WebstartDemo;
import net.tribe7.geom.bezier.BezierCubicLoop;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.GLProgram;
import net.tribe7.opengl.glsl.attribute.GLUFloatMat4;
import net.tribe7.opengl.util.GLViewSize;
import net.tribe7.time.util.ExecutionState;

@WebstartDemo(imageUrl = "http://oglplus.org/oglplus/html/015_graph.png")
public class T015Graph extends GL3EventListener {

	public static final int NODE_COUNT = 512;

	private BezierCubicLoop cameraPath = new BezierCubicLoop(
			new Vector3(-40.0f, -50.0f, -50.0f),
			new Vector3( 40.0f,   0.0f, -60.0f),
			new Vector3( 60.0f,  30.0f,  50.0f),
			new Vector3(-20.0f,  50.0f,  55.0f),
			new Vector3(-30.0f,  30.0f,   0.0f),
			new Vector3(-60.0f,   4.0f, -30.0f)
	);

	private BezierCubicLoop cameraTargetPath = new BezierCubicLoop(
			new Vector3(-10.0f,   0.0f, -10.0f),
			new Vector3( 10.0f,  10.0f, -10.0f),
			new Vector3( 10.0f,   0.0f,  10.0f),
			new Vector3(-10.0f, - 5.0f,  15.0f),
			new Vector3(-10.0f, - 3.0f,   0.0f),
			new Vector3(-10.0f,   0.0f, -10.0f)
	);

	private Angle fov = new Angle();

	private GLProgram p;
	private GLUFloatMat4 uProjectionMatrix, uCameraMatrix;
	private GLVertexArray graphVao = new GLVertexArray();
	private GLBuffer positionsBuffer, edgeIndices;

	private BufferedMatrix4 projMat = new BufferedMatrix4();
	private BufferedMatrix4 camMat = new BufferedMatrix4();

	private double nrand() {
		return 2 * (Math.random() - 0.5);
	}

	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("/net/tribe7/demos/mchochlik/t015Graph/graph.vs", 
				"/net/tribe7/demos/mchochlik/t015Graph/graph.fs", gl);

		graphVao.init(gl);
		p.bind();
		uProjectionMatrix = p.getInterface().getMat4("ProjectionMatrix");
		uCameraMatrix = p.getInterface().getMat4("CameraMatrix");

		float [] positions = new float[NODE_COUNT * 3];
		LinkedList<Integer> edges = new LinkedList<Integer>();

		for (int k = 0; k < positions.length; k++) {
			positions[k++] = (float) (nrand() * 120);
			positions[k++] = (float) (nrand() * 5);
			positions[k++] = (float) (nrand() * 120);
		}

		positionsBuffer = buffer(positions, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 3);
		p.getInterface().getStageAttribute("Position").set(graphVao, positionsBuffer, false, 0).enable();

		for (int i = 0; i < NODE_COUNT; ++i) {

			Vector3 pi = new Vector3(
					positions[i*3+0], 
					positions[i*3+1], 
					positions[i*3+2]);

			double min_dist = 1000.0f;
			int m = i;

			for (int j = i+1; j != NODE_COUNT; ++j) {
				
				Vector3 pj = new Vector3(
						positions[j*3+0], 
						positions[j*3+1], 
						positions[j*3+2]);

				double dist = distance(pi, pj);
				
				if (min_dist > 1.0 && min_dist > dist) {
					min_dist = dist;
					m = j;
				}
			}

			min_dist *= 2.0;
			int done = 0;

			for (int j = i+1; j != NODE_COUNT; ++j) {
				
				Vector3 pj = new Vector3(
						positions[j*3+0],
						positions[j*3+1],
						positions[j*3+2]);

				double dist = distance(pi, pj);

				if (min_dist > dist) {

					double x = dist/min_dist;
					double nRand = nrand();
					
					if (Math.pow(nRand, 2) >= x) {
						edges.addLast(i);
						edges.addLast(j);
						++done;
					}
				}
			}

			if (done == 0) {
				if (i != m) {
					edges.addLast(i);
					edges.addLast(m);
				}
			}

			checkState(edges.size() % 2 == 0);
		}

		int [] edgesArray = toIntArray(edges);
		edgeIndices = buffer(edgesArray, gl, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, 3);

		gl.glClearColor(0.9f, 0.9f, 0.8f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_LINE_SMOOTH);
		gl.glEnable(GL_PROGRAM_POINT_SIZE);
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		graphVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_POINTS, 0, NODE_COUNT * 3);
		getDrawHelper().glIndexedDraw(GL_LINES, edgeIndices);
		graphVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {
		double time = currentState.getElapsedTimeSeconds();
		lookAt(camMat, cameraPath.pointAt(time / 9), cameraTargetPath.pointAt(time / 7));
		uCameraMatrix.set(camMat);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
		perspectiveX(projMat, fov.setDegrees(70), newViewport.width / newViewport.height, 1, 200);
		uProjectionMatrix.set(projMat);
	}
}