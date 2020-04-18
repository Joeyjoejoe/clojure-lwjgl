#version 330 core
in vec3 vertexColor;
in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;
out vec4 FragColor;

uniform vec3 camPos;

void main()
{
  vec3 lightColor = vec3(1.0);
  vec3 lightPos = vec3(0.0, 0.0, 0.0);

  // ambient
  float ambientStrength = 0.3;
  vec3 ambient = ambientStrength * lightColor;

  // diffuse
  vec3 norm = normalize(Normal);
  vec3 lightDir = normalize(lightPos - FragPos);
  float diff = max(dot(norm, lightDir), 0.0);
  vec3 diffuse = diff * lightColor;

  // specular
  // float specularStrength = 0.5;
  // vec3 viewDir = normalize(camPos - FragPos);
  // vec3 reflectDir = reflect(-lightDir, norm);
  // float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
  // vec3 specular = specularStrength * spec * lightColor;

  vec3 result = (ambient + diffuse) * vertexColor;
  FragColor = vec4(result, 1.0);
}
