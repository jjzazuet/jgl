package net.tribe7.swt.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import net.tribe7.newt.shell.test.GLTestShell;
import net.tribe7.swt.GLComposite;
import org.junit.Test;

public class SWTTestShell extends GLTestShell {

	public static final int SHELL_PADDING_WIDTH = 16;
	public static final int SHELL_PADDING_HEIGHT = 40;	

	Display d;
	Shell   s;

	@Test
	public void start() throws Exception {

		d = new Display();
		s = new Shell(d);

		s.setLayout(new FillLayout());
		setSize(s, getWidth(), getHeight());

		// TODO implement test carousel or something... :P
		GLComposite comp = new GLComposite(s, SWT.None, getCapabilities(), getTarget());

		getTarget().getScheduler().setFrameTicksPerSecond(60);
		comp.init();

		s.setText(getShellTitleText());
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