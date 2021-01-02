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
uniform mat4 positionTransformation;

void main()
{
    mat4 worldPosTransformation = aWorldPos * positionTransformation;
    FragPos = vec3(worldPosTransformation * vec4(aPos, 1.0));
    vertexColor = aColor;
    TexCoord = aTexCoord;
    Normal = mat3(transpose(inverse(worldPosTransformation))) * aNormal;
    gl_Position = projection * view * vec4(FragPos, 1.0);
}
