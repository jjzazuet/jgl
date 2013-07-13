package net.tribe7.swt;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.common.base.Throwables.*;
import static org.eclipse.swt.SWT.*;

import javax.media.opengl.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import net.tribe7.opengl.GLScheduledEventListener;
import com.jogamp.opengl.swt.GLCanvas;

public class GLComposite extends Composite implements Listener, Runnable {

	private GLCanvas canvas;
	private final GLScheduledEventListener target;
	private final GLCapabilities caps;

	public GLComposite(Composite parent, int style, GLCapabilities glCaps, 
			GLScheduledEventListener targetListener) {
		super(parent, style);
		target = targetListener;
		caps = glCaps;
	}

	public void init() {
		checkNotNull(caps);
		checkNotNull(target);
		setLayout(new FillLayout());

		canvas = new GLCanvas(this, SWT.None, caps, null, null);
		canvas.addGLEventListener(target);
		canvas.addListener(Paint, this);
		addListener(Dispose, this);
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
			case Paint: run(); break;
			case Dispose: canvas.dispose(); break;
		}
	}
	
	@Override
	public void run() {
		try {
			if (!isDisposed()) {
				canvas.display();
				getDisplay().asyncExec(this);
			}
		} catch (Exception e) { propagate(e); }
	}

	public GLScheduledEventListener getTarget() { return target; }
	public GLCanvas getCanvas() { return canvas; }
}