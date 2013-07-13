package net.tribe7.newt.test;

import net.tribe7.newt.shell.test.GLTestShell;

import org.junit.Test;

import com.jogamp.newt.opengl.GLWindow;

public class NewtTestShell extends GLTestShell {

	@Test
	public void start() throws Exception {

		GLWindow window = GLWindow.create(getCapabilities());

		getTarget().getScheduler().setFrameTicksPerSecond(60);
		window.setTitle(getShellTitleText());
		window.setSize(getWidth(), getHeight());
		window.setVisible(true);
		window.addGLEventListener(getTarget());

		while (true) {
			window.display();
		}
	}
}
