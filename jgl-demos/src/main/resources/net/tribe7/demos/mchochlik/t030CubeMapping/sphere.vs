#version 330

uniform float Time;
uniform mat4 CameraMatrix, ProjectionMatrix;
uniform vec4 LightPos;
in vec4 Position;
in vec3 Normal, Tangent;
in vec2 TexCoord;
out vec3 vertNormal;
out vec3 vertLight;
out vec3 vertRefl;

void main(void) {

	float amps = 0.02, ampt = 0.05/(1.0+Time*0.1);
	float pers = 6.0, pert = 3.0+Time*0.1;
	float vels = 4.0, velt = 3.0;
	float as = (TexCoord.s + Time/vels)*3.1415*2.0;
	float at = (TexCoord.t + Time/velt)*3.1415*2.0;

	vec3 Bitangent = cross(Normal, Tangent);
	vec2 Sin = vec2(sin(as*pers), sin(at*pert));
	vec2 Cos = vec2(cos(as*pers), cos(at*pert));

	vertNormal = normalize(
		Normal - Tangent * cos(as*pers)*amps*5.0+
		Normal - Bitangent* cos(at*pert)*ampt*5.0 
	);
	gl_Position = vec4(
		Position.xyz + Normal * (
			amps * sin(as*pers)+
			ampt * sin(at*pert) 
		), 1.0
	);
	vertLight = LightPos.xyz - gl_Position.xyz;
	vertRefl = reflect(
			-vec3(
				CameraMatrix[0][2],
				CameraMatrix[1][2],
				CameraMatrix[2][2] 
			),
			normalize(vertNormal)
	);
	gl_Position =
		ProjectionMatrix *
		CameraMatrix *
		gl_Position;
}