#version 330

uniform uint ViewportWidth, ViewportHeight;
in vec4 Position;
out vec2 vertTexCoord;

void main(void) {
	gl_Position = Position;
	vertTexCoord = vec2(
		(Position.x*0.5 + 0.5)*ViewportWidth,
		(Position.y*0.5 + 0.5)*ViewportHeight
	);
}
