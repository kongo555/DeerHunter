package deerhunter.shaders;


public class SunShadowShader extends Shader { // FIXME moze zmodyfikowac zeby nie renderowalo za postacia
	public SunShadowShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void setUniformf(float lightIntensity){
		shader.begin();
		shader.setUniformf("u_lightIntensity", lightIntensity);//0-1
		shader.end(); 
	}
}