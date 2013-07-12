#version 330

uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;
in vec4 Position;
in vec2 TexCoord;
out vec2 vertTexCoord;

void main(void) {
	vertTexCoord = TexCoord;
	gl_Position = 
		ProjectionMatrix *
		CameraMatrix *
		ModelMatrix *
		Position;
}