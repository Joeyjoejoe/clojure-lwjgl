#version 330 core

out vec4 outColor;

in vec4 vertexColor;

void main()
{
  outColor = vertexColor;
}
