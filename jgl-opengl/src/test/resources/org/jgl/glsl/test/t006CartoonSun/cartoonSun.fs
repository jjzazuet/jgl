#version 330

in vec2 vertPos;
out vec3 fragColor;

uniform float Time;
uniform vec2 SunPos;
uniform vec3 Sun1, Sun2, Sky1, Sky2;

void main(void) {
	vec2 v = vertPos - SunPos;
	float l = length(v);
	float a = atan(v.y, v.x)/3.1415;
	if(l < 0.1) { fragColor = Sun1; }
	else if(int(18*(Time*0.1 + 1.0 + a)) % 2 == 0) {
		fragColor = mix(Sun1, Sun2, l);
	}
	else fragColor = mix(Sky1, Sky2, l);
}