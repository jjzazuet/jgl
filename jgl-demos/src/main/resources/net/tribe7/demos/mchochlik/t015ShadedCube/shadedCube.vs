#version 330

uniform mat4 ProjectionMatrix, CameraMatrix;
in vec4 Position;
in vec3 Normal;
out vec3 vertColor;
out vec3 vertNormal;

void main(void) {
	vertNormal = (
		CameraMatrix * vec4(
			normalize(Normal + Position.xyz*0.5), 0.0
		)
	).xyz;
	
	vertColor = abs(Normal);
	gl_Position = ProjectionMatrix * CameraMatrix * Position;
}