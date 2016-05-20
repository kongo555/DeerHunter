package deerhunter.shaders;

public class ShadowMapShader extends Shader {
	public ShadowMapShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void setUniformf(int resolution, float upScale){
		shader.begin();
		shader.setUniformf("resolution", resolution, resolution);
		shader.setUniformf("upScale", upScale);
		shader.end(); 
	}
}
