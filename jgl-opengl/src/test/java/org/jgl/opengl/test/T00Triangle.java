package org.jgl.opengl.test;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import org.jgl.opengl.GLScheduledEventListener;
import org.jgl.opengl.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T00Triangle extends GLScheduledEventListener {

	@Override
	protected void doInit(GLAutoDrawable gad) {
		setup((GL2) gad.getGL());
	}
	
	@Override
	public void doRender(GLAutoDrawable gad, ExecutionState es) throws Exception {
		
		GL2 gl2 = (GL2) gad.getGL();
		
		gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

		// draw a triangle filling the window
		gl2.glLoadIdentity();
		gl2.glBegin(GL.GL_TRIANGLES);
		gl2.glColor3f(1, 0, 0);
		gl2.glVertex2f(0, 0);
		gl2.glColor3f(0, 1, 0);
		gl2.glVertex2f(getGlViewSize().width, 0);
		gl2.glColor3f(0, 0, 1);
		gl2.glVertex2f(getGlViewSize().width / 2, getGlViewSize().height);
		gl2.glEnd();
	}

	@Override
	protected void doUpdate(GLAutoDrawable gad, ExecutionState es) throws Exception {
		System.err.println(es.renderTimer.getFps());
	}

	@Override
	protected void onResize(GLAutoDrawable gad, GlViewSize newViewport) {
		setup((GL2) gad.getGL());
	}

	protected void setup(GL2 gl2) {
		
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();

		GLU glu = new GLU();
		
		glu.gluOrtho2D(0.0f, getGlViewSize().width, 0.0f, getGlViewSize().height);
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glLoadIdentity();
		gl2.glViewport(getGlViewSize().x, getGlViewSize().y, 
				getGlViewSize().width, getGlViewSize().height);	
	}
}
