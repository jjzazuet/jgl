package net.tribe7.shell;

import org.junit.Test;
import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;

public class NewtShell extends GLTestShell {

	@Test
	public void start() throws Exception {

		GLWindow window = GLWindow.create(getCapabilities());
		RunListener rl = new RunListener();

		window.setTitle(getShellTitleText());
		window.setSize(getWidth(), getHeight());
		window.setVisible(true);
		window.addGLEventListener(getTarget());
		window.addWindowListener(rl);

		while (rl.isRun()) { window.display(); }
	}
}

class RunListener extends WindowAdapter {
	private boolean run = true;
	@Override
	public void windowDestroyed(WindowEvent e) { run = false; }
	public boolean isRun() { return run; }	
}