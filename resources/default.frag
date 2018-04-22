#version 330 core

out vec4 outColor;

in vec4 vertexColor;
in vec2 TexCoord;

//uniform sampler2D texture1;
//uniform sampler2D texture2;

void main()
{
  //outColor = mix(texture(texture1, TexCoord), texture(texture2, TexCoord), 0.2) * vertexColor;
  outColor = vertexColor;
}
