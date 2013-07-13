#version 330

uniform int ColorCount;
uniform vec4 Color[8];
in vec3 vertNormal;
vec3 ViewDir = vec3(0.0, 0.0, 1.0);
vec3 TopDir = vec3(0.0, 1.0, 0.0);
out vec4 fragColor;

void main(void) {

	float k = dot(vertNormal, ViewDir);
	vec3 reflDir = 2.0*k*vertNormal - ViewDir;
	float a = dot(reflDir, TopDir);
	vec3 reflColor;

	for(int i = 0; i != (ColorCount - 1); ++i) {

		if(a<Color[i].a && a>=Color[i+1].a) {
			float m = 
				(a - Color[i].a)/
				(Color[i+1].a-Color[i].a);
			reflColor = mix(
				Color[i].rgb,
				Color[i+1].rgb,
				m
			);
			break;
		}
	}
	
	float i = max(dot(vertNormal, TopDir), 0.0);
	vec3 diffColor = vec3(i, i, i);
	
	fragColor = vec4(
		mix(reflColor, diffColor, 0.3 + i*0.7),
		1.0
	);
}