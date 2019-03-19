#version 330 core

out vec4 outColor;
in vec3 vertexColor;

void main()
{
  outColor = vec4(vertexColor, 1.0);
}
