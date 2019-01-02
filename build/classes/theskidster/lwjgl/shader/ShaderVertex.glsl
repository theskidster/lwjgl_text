#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoords;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;
uniform vec2 uTexOffset;

out vec2 ioTexCoords;

void main() {
    gl_Position = uProj * uView * uModel * vec4(aPosition, 1.0);
    ioTexCoords = aTexCoords + uTexOffset;
}