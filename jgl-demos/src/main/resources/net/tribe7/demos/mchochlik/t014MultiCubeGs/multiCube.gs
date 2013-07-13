#version 330

layout(triangles) in;
layout(triangle_strip, max_vertices = 108) out;
uniform mat4 ProjectionMatrix, CameraMatrix;
out vec3 vertColor;

void main(void) {

	for(int c=0; c!=36; ++c) {
	
		float angle = c * 10 * 2 * 3.14159 / 360.0;
		float cx = cos(angle);
		float sx = sin(angle);
	
		mat4 ModelMatrix = mat4(
			 cx, 0.0,  sx, 0.0,
			0.0, 1.0, 0.0, 0.0,
			-sx, 0.0,  cx, 0.0,
			0.0, 0.0, 0.0, 1.0 
		) * mat4(
			 1.0, 0.0, 0.0, 0.0,
			 0.0, 1.0, 0.0, 0.0,
			 0.0, 0.0, 1.0, 0.0,
			12.0, 0.0, 0.0, 1.0 
		);
	
		for(int v=0; v!=gl_in.length(); ++v) {
			
			vec4 vert = gl_in[v].gl_Position;
			gl_Position = 
				ProjectionMatrix *
				CameraMatrix *
				ModelMatrix *
				vert;
			vertColor = abs(normalize(ModelMatrix*vert)).xzy;
			EmitVertex();
		}
		
		EndPrimitive();
	}
}