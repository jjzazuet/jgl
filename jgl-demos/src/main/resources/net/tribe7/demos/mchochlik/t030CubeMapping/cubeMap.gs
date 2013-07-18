#version 330

layout(triangles) in;
layout(triangle_strip, max_vertices = 18) out;
uniform mat4 ProjectionMatrix;

const mat4 CubeFaceMatrix[6] = mat4[6](
	mat4(
		 0.0,  0.0, -1.0,  0.0,
		 0.0, -1.0,  0.0,  0.0,
		-1.0,  0.0,  0.0,  0.0,
		 0.0,  0.0,  0.0,  1.0 
	), mat4(
		 0.0,  0.0,  1.0,  0.0,
		 0.0, -1.0,  0.0,  0.0,
		 1.0,  0.0,  0.0,  0.0,
		 0.0,  0.0,  0.0,  1.0 
	), mat4(
		 1.0,  0.0,  0.0,  0.0,
		 0.0,  0.0, -1.0,  0.0,
		 0.0,  1.0,  0.0,  0.0,
		 0.0,  0.0,  0.0,  1.0 
	), mat4(
		 1.0,  0.0,  0.0,  0.0,
		 0.0,  0.0,  1.0,  0.0,
		 0.0, -1.0,  0.0,  0.0,
		 0.0,  0.0,  0.0,  1.0 
	), mat4(
		 1.0,  0.0,  0.0,  0.0,
		 0.0, -1.0,  0.0,  0.0,
		 0.0,  0.0, -1.0,  0.0,
		 0.0,  0.0,  0.0,  1.0 
	), mat4(
		-1.0,  0.0,  0.0,  0.0,
		 0.0, -1.0,  0.0,  0.0,
		 0.0,  0.0,  1.0,  0.0,
		 0.0,  0.0,  0.0,  1.0 
	)
);

in vec3 tempColor[];
in vec3 tempNormal[];
in vec3 tempLight[];
out vec3 vertColor;
out vec3 vertNormal;
out vec3 vertLight;

void main(void) {
	for(gl_Layer=0; gl_Layer!=6; ++gl_Layer)
	{
		for(int i=0; i!=3; ++i)
		{
			gl_Position = 
				ProjectionMatrix *
				CubeFaceMatrix[gl_Layer]*
				gl_in[i].gl_Position;
			vertColor = tempColor[i];
			vertNormal = tempNormal[i];
			vertLight = tempLight[i];
			EmitVertex();
		}
		EndPrimitive();
	}
}