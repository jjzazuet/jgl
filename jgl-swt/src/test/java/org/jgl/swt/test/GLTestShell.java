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

	Display d;
	Shell   s;
	
	@Test
	public void start() throws Exception {
		
		JoglNativeBoostrap.joglBootstrap();
		
		d = new Display();
		s = new Shell(d);
		
		s.setLayout(new FillLayout());
		s.setSize(816, 640);
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
		
		caps.setDoubleBuffered(true);
		caps.setNumSamples(8);
		caps.setSampleBuffers(false);

		GLComposite comp = new GLComposite(s, SWT.None, caps, new T013SpiralSphere());
		
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
}
