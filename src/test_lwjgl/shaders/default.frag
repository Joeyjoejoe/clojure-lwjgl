#version 330 core

out vec4 outColor;
in vec4 vertexColor;

uniform vec4 uniformColor;

void main()
{
  outColor = vertexColor;
} 
