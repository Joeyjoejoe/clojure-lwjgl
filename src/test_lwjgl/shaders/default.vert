#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTexCoord;

out vec4 vertexColor;
out vec2 TexCoord;

uniform mat4 rotate;

void main()
{
    gl_Position = rotate * vec4(aPos, 1.0);
    vertexColor = vec4(aColor, 1.0);
    TexCoord = aTexCoord;
}
