package net.tribe7.shell;

import com.jogamp.newt.awt.applet.JOGLNewtApplet1Run;

public class JglApplet extends JOGLNewtApplet1Run {

	public static final String JGL_LISTENER_FRAME_TICKS_PER_SECOND = "jgl_listener_frame_ticks_per_second";
	private static final long serialVersionUID = 4766618351409503056L;

	@Override
	public void init() {
		String listenerFps = getParameter(JGL_LISTENER_FRAME_TICKS_PER_SECOND);
		super.init();
		
	}
}
