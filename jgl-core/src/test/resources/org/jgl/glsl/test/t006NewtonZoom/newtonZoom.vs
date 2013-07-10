#version 330 

uniform mat2 ZoomMatrix; 
in vec2 Position; 
out vec2 vertCoord; 

void main(void) { 
	vertCoord = ZoomMatrix * Position; 
	gl_Position = vec4(Position, 0.0, 1.0); 
}