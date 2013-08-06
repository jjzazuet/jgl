#version 330

uniform vec2 ScreenSize;

in vec4 Position;
in vec2 TexCoord;

out vec2 vertTexCoord;

void main(void) {
	gl_Position = Position;
	vertTexCoord = ScreenSize*TexCoord;
}