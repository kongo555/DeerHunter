#ifdef GL_ES
#define LOWP lowp
	precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main(){
	 vec3 color = texture2D(u_texture, v_texCoords).rgb;
     float gray = (color.r + color.g + color.b) / 3.0;
     vec3 grayscale = vec3(gray);

	gl_FragColor = vec4(grayscale, 1.0);
}
