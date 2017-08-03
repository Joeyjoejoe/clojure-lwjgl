#version 330 core

out vec4 outColor;

in vec4 vertexColor;
in vec2 TexCoord;

uniform sampler2D ourTexture;
uniform vec4 uniformColor;

void main()
{
  outColor = texture(ourTexture, TexCoord) * vertexColor; 
} 
