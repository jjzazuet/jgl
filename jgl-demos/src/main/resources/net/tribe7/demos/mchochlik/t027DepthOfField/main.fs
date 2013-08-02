#version 330

uniform vec3 AmbientColor, DiffuseColor;
in vec3 vertLightDir;
in vec3 vertNormal;
out vec4 fragColor;

void main(void) {
	float d = max(dot(vertLightDir,vertNormal),0.0);
	float e = sin(
		10.0*vertLightDir.x + 
		20.0*vertLightDir.y + 
		25.0*vertLightDir.z   
	)*0.9;
	fragColor = vec4(
		mix(AmbientColor, DiffuseColor, d+e),
		1.0
	);
}
