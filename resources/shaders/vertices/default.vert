#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in vec3 aNormal;
layout (location = 4) in mat4 aWorldPos;

out vec3 vertexColor;
out vec2 TexCoord;
out vec3 Normal;
out vec3 FragPos;

uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * aWorldPos * vec4(aPos, 1.0);
    vertexColor = aColor;
    TexCoord = aTexCoord;
    Normal = aNormal;
    FragPos = vec3(aWorldPos * vec4(aPos, 1.0));
}
