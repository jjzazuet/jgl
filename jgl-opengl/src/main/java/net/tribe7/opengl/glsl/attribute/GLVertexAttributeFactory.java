package net.tribe7.opengl.glsl.attribute;

import static java.lang.String.format;
import static javax.media.opengl.GL2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH;
import static net.tribe7.common.base.Preconditions.checkNotNull;
import static net.tribe7.opengl.glsl.attribute.GLAttributeBuffers.ZERO;
import static net.tribe7.opengl.util.GLSLUtils.getGlslParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tribe7.opengl.glsl.GLProgram;

public class GLVertexAttributeFactory {

	private static final Logger log = LoggerFactory.getLogger(GLVertexAttributeFactory.class);

	protected static GLVertexAttribute newStageAttribute(int index, GLProgram p) {

		checkNotNull(p);
		GLVertexAttribute at = null;
		GLAttributeBuffers ab = new GLAttributeBuffers(index, getGlslParam(p, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH));

		p.getGl().glGetActiveAttrib(p.getGlResourceHandle(), ab.getAttributeIndex(), ab.getName().limit(), ab.getLength(), ab.getSize(), ab.getType(), ab.getName());
		p.checkError();
		int location = p.getGl().glGetAttribLocation(p.getGlResourceHandle(), ab.getNameValue());
		p.checkError();

		if (location < ZERO) {
			log.warn(p.resourceMsg(format("Invalid active attribute: [%s, %s, %s, %s]", 
					location, ab.getSizeValue(), Integer.toHexString(ab.getTypeValue()), ab.getNameValue())));
		} else {
			at = new GLVertexAttribute(ab.getAttributeIndex(), location, ab.getSizeValue(), ab.getTypeValue(), ab.getNameValue(), p);
		}
		return at;
	}
}
