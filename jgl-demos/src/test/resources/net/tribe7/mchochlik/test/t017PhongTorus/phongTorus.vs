#version 330

uniform mat4 ProjectionMatrix, CameraMatrix;
in vec4 Position;
in vec3 Normal;
out vec3 vertNormal;
out vec3 vertViewDir;

void main(void) {
	vertNormal = Normal;
	vertViewDir = (vec4(0.0, 0.0, 1.0, 1.0)*CameraMatrix).xyz;
	gl_Position = ProjectionMatrix * CameraMatrix * Position;
}