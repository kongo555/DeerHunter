package deerhunter.shaders;

public class ShadowRenderShader extends Shader {
	public ShadowRenderShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void setUniformf(int resolution, boolean softShadows){
		shader.begin();
		shader.setUniformf("resolution", resolution, resolution);
		shader.setUniformf("softShadows", softShadows ? 1f : 0f);
		shader.end(); 
	}
}
