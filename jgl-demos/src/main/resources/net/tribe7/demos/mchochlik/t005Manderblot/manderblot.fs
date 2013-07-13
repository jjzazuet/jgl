#version 330 

in vec2 vertCoord; 
out vec4 fragColor; 
const int nclr = 5; 
uniform vec4 clrs[5]; 

void main(void) {
 
	vec2 z = vec2(0.0, 0.0); 
	vec2 c = vertCoord; 
	int i = 0, max = 128; 

	while((i != max) && (distance(z, c) < 2.0)) { 
		vec2 zn = vec2( 
			z.x * z.x - z.y * z.y + c.x, 
			2.0 * z.x * z.y + c.y 
		); 
		z = zn; 
		++i; 
	} 
	
	float a = sqrt(float(i) / float(max));
	 
	for(i = 0; i != (nclr - 1); ++i) { 
		if(a >= clrs[i].a && a < clrs[i+1].a) { 
			float m = (a - clrs[i].a) / (clrs[i+1].a - clrs[i].a); 
			fragColor = vec4( 
				mix(clrs[i].rgb, clrs[i+1].rgb, m), 
				1.0 
			); 
			break; 
		} 
	} 
}