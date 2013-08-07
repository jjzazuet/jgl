package net.tribe7.opengl.glsl;

import static net.tribe7.common.base.Preconditions.*;
import java.util.*;
import net.tribe7.opengl.glsl.attribute.*;

public class GLProgramInterface extends GLUniformInterface {

	private final Map<String, GLProgramVariable> stageAttributes = new HashMap<String, GLProgramVariable>();
	private final Map<String, GLProgramVariable> uniformBlocks = new HashMap<String, GLProgramVariable>();

	public GLVertexAttribute getStageAttribute(String name) {
		checkNotNull(name);
		return (GLVertexAttribute) checkNotNull(stageAttributes.get(name)); 
	}

	public GLUniformBlock getUniformBlock(String name) {
		checkNotNull(name);
		return (GLUniformBlock) checkNotNull(uniformBlocks.get(name));
	}

	public Map<String, GLProgramVariable> getUniformBlocks() { return uniformBlocks; }
	public Map<String, GLProgramVariable> getStageAttributes() { return stageAttributes; }
}
