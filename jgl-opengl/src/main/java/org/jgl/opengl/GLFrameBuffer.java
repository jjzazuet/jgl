package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;
import java.util.*;

public class GLFrameBuffer extends GLContextBoundResource {

	private Map<Integer, GLTexture2D> colorAttachments = new HashMap<Integer, GLTexture2D>();
	private GLTextureMetadata colorAttachmentFormat;
	
	public void setColorAttachment(int colorAttachmentIndex) {

		checkState(getColorAttachmentFormat() != null, "Color attachment format not set!");
		checkArgument(colorAttachmentIndex >= ZERO);
		checkArgument(
				!getColorAttachments().containsKey(colorAttachmentIndex),
				"Color attachment already set!");
		colorAttachments.put(colorAttachmentIndex, new GLTexture2D());
	}

	@Override
	protected void doInit() {

		IntBuffer ib = IntBuffer.allocate(ONE);
		getGl().glGenFramebuffers(ONE, ib);
		setGlResourceHandle(ib.get());
		checkError();

		// initialize color attachments
	}

	@Override
	protected void doBind() {

	}

	@Override
	protected void doUnbind() {

	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteFramebuffers(ONE, intBuffer(getGlResourceHandle()));
	}

	public Map<Integer, GLTexture2D> getColorAttachments() { return colorAttachments; }
	public GLTextureMetadata getColorAttachmentFormat() { return colorAttachmentFormat; }

	public void setColorAttachmentFormat(GLTextureMetadata colorAttachmentFormat) {
		this.colorAttachmentFormat = checkNotNull(colorAttachmentFormat);
	}
}
