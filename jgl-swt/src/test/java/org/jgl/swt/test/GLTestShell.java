package org.jgl.swt.test;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

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
		s.setSize(512, 384);
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
		
		caps.setDoubleBuffered(true);
		caps.setNumSamples(8);
		caps.setSampleBuffers(false);

		GLComposite comp = new GLComposite(s, SWT.None, caps, new T01Triangle());
		
		comp.getScheduler().setFrameTicksPerSecond(1);
		comp.init();
		s.open();

		while (!s.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}

		d.dispose();
	}
}
