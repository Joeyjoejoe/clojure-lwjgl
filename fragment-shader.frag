#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution; // taille du Canvas (x:largeur en pixels, y:hauteur en pixels)
uniform vec2 u_mouse;      // position de la souris (x,y) sur le canvas en pixels
uniform float u_time;     // temps écoulé depuis le lancement du shader

vec2 normalized_coord = gl_FragCoord.xy/u_resolution;
float normalized_time = abs(sin(u_time));
float x_max = u_mouse.x + 25.0;
float x_min = u_mouse.x - 25.0;
float y_max = u_mouse.y + 25.0;
float y_min = u_mouse.y - 25.0;
vec4 m_range = vec4(gl_FragCoord.x - 25.0, gl_FragCoord.x + 25.0, gl_FragCoord.y - 25.0, gl_FragCoord.y + 25.0); 

vec4 color(){
  if(u_mouse.x >= m_range[0] && u_mouse.x <= m_range[1] && u_mouse.y >= m_range[2] && u_mouse.y <= m_range[3]){
    float degrade = ((((gl_FragCoord.x - x_min) / (x_max - x_min)) * ((gl_FragCoord.y - y_min) / (y_max - y_min))) / 2.0);
    if(degrade > 0.5){
      return vec4(1.0,1.0, 0.0, 1.0 - degrade );
    }else{
      return vec4(1.0,1.0, 0.0, degrade );
    }
  }else{
    return vec4(0.0, 0.0, 0.0, 1.0);
  }
}

void main(){
  gl_FragColor = color();
}
