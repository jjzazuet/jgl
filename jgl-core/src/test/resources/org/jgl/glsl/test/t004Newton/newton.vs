#version 330

in vec2 Position; 
out vec2 vertCoord; 

void main(void) {
	vertCoord = Position; 
	gl_Position = vec4(Position, 0.0, 1.0); 
}