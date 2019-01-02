#version 330 core

in vec2 ioTexCoords;

uniform sampler2D tex;

out vec4 ioResult;

void main() {
    vec4 finalTexture = texture(tex, ioTexCoords);
    if(finalTexture.a == 0) discard;
    ioResult = finalTexture;
}