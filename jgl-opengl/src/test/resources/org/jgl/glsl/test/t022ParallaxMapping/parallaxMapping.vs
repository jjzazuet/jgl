#version 330

uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;
uniform vec3 LightPos;

in vec4 Position;
in vec3 Normal;
in vec3 Tangent;
in vec2 TexCoord;

out vec3 vertEye;
out vec3 vertLight;
out vec3 vertNormal;
out vec2 vertTexCoord;
out vec3 vertViewTangent;
out mat3 NormalMatrix;

void main(void){
	vec4 EyePos = CameraMatrix * ModelMatrix * Position;
	vertEye = EyePos.xyz;
	vec3 fragTangent = (CameraMatrix * ModelMatrix * vec4(Tangent, 0.0)).xyz;
	vertNormal = (CameraMatrix * ModelMatrix * vec4(Normal, 0.0)).xyz;
	vertLight = (CameraMatrix * vec4(LightPos-vertEye, 1.0)).xyz;
	NormalMatrix = mat3(fragTangent, cross(vertNormal, fragTangent), vertNormal);
	vertViewTangent = vec3(
		dot(NormalMatrix[0], vertEye),
		dot(NormalMatrix[1], vertEye),
		dot(NormalMatrix[2], vertEye) 
	);
	vertTexCoord = TexCoord;
	gl_Position = ProjectionMatrix * EyePos;
}