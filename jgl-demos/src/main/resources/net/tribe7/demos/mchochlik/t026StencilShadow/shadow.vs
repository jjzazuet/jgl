#version 330

in vec4 Position;
in vec3 Normal;
uniform mat4 ModelMatrix;
uniform vec3 LightPos;
out float ld;

void main(void) {
	gl_Position = ModelMatrix * Position;
	vec3 geomNormal = mat3(ModelMatrix)*Normal;
	vec3 lightDir = LightPos - gl_Position.xyz;
	ld = dot(geomNormal, normalize(lightDir));
}