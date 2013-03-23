#version 330

in vec2 Position;
out vec2 vertPos;

void main(void) {
	gl_Position = vec4(Position, 0.0, 1.0);
	vertPos = gl_Position.xy;
}