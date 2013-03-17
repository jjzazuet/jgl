package org.jgl.swt;

import static org.jgl.time.util.StateMethod.*;
import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Throwables.*;
import static org.eclipse.swt.SWT.*;

import javax.media.opengl.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.jgl.opengl.GLScheduledEventListener;
import org.jgl.time.*;
import org.jgl.time.util.ExecutionState;

import com.jogamp.opengl.swt.GLCanvas;

public class GLComposite extends Composite implements Listener, Runnable, RenderStateListener {

	private GLCanvas canvas;
	private final GLScheduledEventListener target;
	private final GLCapabilities caps;
	private final Scheduler scheduler = new FixedTimeStepScheduler();

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
		scheduler.setStateListener(this);
		scheduler.init();
		
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
				scheduler.stateTick();
				getDisplay().asyncExec(this);				
			}
		} catch (Exception e) { propagate(e); }
	}

	@Override
	public void updateTick(double elapsedTimeUs, double frameTimeUs) throws Exception {
		ExecutionState es = target.getExecutionState();
		es.logicTimer.update();
		es.method = UPDATE;
		es.elapsedTimeUs = elapsedTimeUs;
		es.frameTimeUs = frameTimeUs;
		canvas.display();
	}
	
	@Override
	public void renderTick(double tickTimeUs, double tickDelta) throws Exception {
		ExecutionState es = target.getExecutionState();
		es.renderTimer.update();
		es.method = RENDER;
		es.tickTimeUs = tickTimeUs;
		es.tickDelta = tickDelta;
		canvas.display();
	}
	
	public Scheduler getScheduler() { return scheduler; }
}