#version 330 core
in vec3 vertexColor;
out vec4 FragColor;

uniform vec3 lightColor;

void main()
{
  float ambientStrength = 0.5;
  vec3 ambient = ambientStrength * vec3(1.0);
  vec3 result = ambient * vertexColor;
  FragColor = vec4(result, 1.0);
}
