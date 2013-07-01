package org.jgl.swt.test;

import javax.media.opengl.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.jgl.opengl.test.*;
import org.jgl.opengl.util.JoglNativeBoostrap;
import org.jgl.swt.GLComposite;
import org.junit.Test;

public class GLTestShell {

	public static final int SHELL_PADDING_WIDTH = 16;
	public static final int SHELL_PADDING_HEIGHT = 40;	

	Display d;
	Shell   s;

	@Test
	public void start() throws Exception {

		JoglNativeBoostrap.joglBootstrap();

		d = new Display();
		s = new Shell(d);

		s.setLayout(new FillLayout());
		setSize(s, 800, 600);

		GLProfile profile = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(profile);

		caps.setDoubleBuffered(true);
		caps.setNumSamples(8);
		caps.setSampleBuffers(true);

		// TODO implement test carousel or something... :P
		GLComposite comp = new GLComposite(s, SWT.None, caps, new T017PhongTorus());

		comp.getScheduler().setFrameTicksPerSecond(60);
		comp.init();

		s.setText(String.format("GLTestShell [%s]", comp.getTarget().getClass().getName()));
		s.open();

		while (!s.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}

		d.dispose();
	}

	// TODO this should be platform specific :P
	public void setSize(Shell target, int width, int height) {
		target.setSize(width + SHELL_PADDING_WIDTH, height + SHELL_PADDING_HEIGHT);
	}
}
