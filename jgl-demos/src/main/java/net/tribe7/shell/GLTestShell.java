package net.tribe7.shell;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.common.base.Throwables.*;
import javax.media.opengl.*;

import net.tribe7.demos.mchochlik.*;
import net.tribe7.opengl.GLScheduledEventListener;
import net.tribe7.opengl.util.GLBootstrap;
import com.jogamp.common.jvm.JNILibLoaderBase;

public class GLTestShell {

	private int width = 800;
	private int height = 600;
	private final GLProfile profile;
	private final GLCapabilities capabilities;
	private GLScheduledEventListener target;

	public GLTestShell() {

		try { JNILibLoaderBase.setLoadingAction(new GLBootstrap()); }
		catch (Exception e) { propagate(e); }
		profile = GLProfile.get(GLProfile.GL3);
		capabilities = new GLCapabilities(profile);

		getCapabilities().setDoubleBuffered(true);
		getCapabilities().setNumSamples(8);
		getCapabilities().setSampleBuffers(false);

		GLScheduledEventListener target = new T006NewtonZoom();
		target.getScheduler().setFrameTicksPerSecond(60);
		setTarget(target);
	}

	public GLProfile getProfile() { return profile; }
	public GLCapabilities getCapabilities() { return capabilities; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public GLScheduledEventListener getTarget() { return target; }
	public void setTarget(GLScheduledEventListener target) {
		this.target = checkNotNull(target);
	}

	public String getShellTitleText() {
		return String.format("%s [%s]", 
				getClass().getSimpleName(),
				getTarget().getClass().getName());
	}
}
