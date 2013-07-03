package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;
import java.util.*;

public class GLFrameBuffer extends GLContextBoundResource {

	private Map<Integer, GLTexture2DImage> colorAttachments = new HashMap<Integer, GLTexture2DImage>();
	private GLTextureMetadata colorAttachmentFormat;
	
	public void createColorAttachment(int glColorAttachmentTarget) {

		checkState(getColorAttachmentFormat() != null, "Color attachment format not set!");
		checkArgument(glColorAttachmentTarget >= ZERO);
		checkArgument(
				!getColorAttachments().containsKey(glColorAttachmentTarget), 
				"Color attachment already set!");
		
		GLTexture2D colorAttachment = new GLTexture2D();

		colorAttachment.init(getGl());
		//colorAttachment.set
	}

	@Override
	protected void doInit() {
		IntBuffer ib = IntBuffer.allocate(ONE);
		getGl().glGenFramebuffers(ONE, ib);
		setGlResourceHandle(ib.get());
		checkError();
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

	public Map<Integer, GLTexture2DImage> getColorAttachments() { return colorAttachments; }
	public GLTextureMetadata getColorAttachmentFormat() { return colorAttachmentFormat; }

	public void setColorAttachmentFormat(GLTextureMetadata colorAttachmentFormat) {
		this.colorAttachmentFormat = checkNotNull(colorAttachmentFormat);
	}
}
