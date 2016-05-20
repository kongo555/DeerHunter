package deerhunter.shaders;

public class NightShader extends Shader {
	public NightShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void setUniformf(float time0to1){
		shader.begin();
		shader.setUniformf("time", time0to1);
		shader.end(); 
	}
}
