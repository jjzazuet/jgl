package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static net.tribe7.opengl.glsl.attribute.GLUniformAttributeFactory.*;
import static net.tribe7.opengl.glsl.attribute.GLUniformBlockFactory.*;
import static net.tribe7.opengl.glsl.attribute.GLVertexAttributeFactory.*;

import java.util.*;
import net.tribe7.opengl.glsl.*;
import org.slf4j.*;

public class GLAttributeFactory {

	private static final Logger log = LoggerFactory.getLogger(GLUniformAttribute.class);

	public static final Map<String, GLProgramInterface> getAttributeMap(int type, GLProgram p) {

		checkArgument(isValidActiveAttributeType(type));
		int objectCount = getGlslParam(p, type);
		Map<String, GLProgramInterface> programAttributes = new HashMap<String, GLProgramInterface>();
		GLProgramInterface at = null;

		for (int k = 0; k < objectCount; k++) {
			switch (type) {
				case GL_ACTIVE_UNIFORMS: at = newUniformAttribute(k, false, p); break;
				case GL_ACTIVE_UNIFORM_BLOCKS: at = newUniformAttributeBlock(k, p);  break;
				case GL_ACTIVE_ATTRIBUTES: at = newStageAttribute(k, p); break;
			}
			if (at != null) {
				if (log.isDebugEnabled()) { log.debug(at.toString()); }
				programAttributes.put(at.getName(), at);
			}
		}
		return programAttributes;
	}

	public static boolean isValidActiveAttributeType(int glAttributeType) {
		return glAttributeType == GL_ACTIVE_UNIFORMS 
				|| glAttributeType == GL_ACTIVE_UNIFORM_BLOCKS 
				|| glAttributeType == GL_ACTIVE_ATTRIBUTES;
	}
}
