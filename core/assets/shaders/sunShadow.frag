#ifdef GL_ES
#define LOWP lowp
	precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;

varying vec2 v_texCoords;
varying vec4 v_position;

uniform sampler2D u_texture;
uniform float u_lightIntensity;

void main(){
	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	if(gl_FragColor.a >0.1){
		gl_FragColor = vec4(0.0,0.0,0.0,u_lightIntensity);
	}
}
