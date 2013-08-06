#version 330
uniform mat4 ProjectionMatrix, CameraMatrix, SingleModelMatrix;
uniform uint SingleModel;
layout (std140) uniform ModelBlock {
	mat4 ModelMatrices[128];
	mat4 baz;
	vec2 foo;
	vec3 bar[2];
};

const vec3 LightPos = vec3(0.0, 0.0, 0.0);

in vec4 Position;
in vec3 Normal;
in vec2 TexCoord;

out vec3 vertLightDir;
out vec3 vertNormal;
out vec3 vertColor;
out vec2 vertTexCoord;

void main(void) {

	mat4 ModelMatrix = 
		SingleModel!=0u?
		SingleModelMatrix:
		ModelMatrices[gl_InstanceID];

	gl_Position = ModelMatrix * Position;
	vertLightDir = normalize(LightPos - gl_Position.xyz);
	vertNormal = mat3(ModelMatrix)*Normal;
	vertTexCoord = SingleModel!=0u ? Position.xz : TexCoord;
	vertColor = abs(normalize((ModelMatrix*Position).yxz));
	gl_Position = ProjectionMatrix * CameraMatrix * gl_Position;
}