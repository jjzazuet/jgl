#version 330 

in vec2 Position; 
in vec2 Coord; 
out vec2 vertCoord; 

void main(void) { 
	vertCoord = Coord; 
	gl_Position = vec4(Position, 0.0, 1.0); 
}