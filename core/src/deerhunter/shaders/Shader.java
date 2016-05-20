package deerhunter.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import deerhunter.utilities.Utility;

public abstract class Shader {
	protected ShaderProgram shader;
	
	public Shader(String vertexShader, String fragmentShader){
		shader.pedantic = false;
		shader = Utility.loadShader(vertexShader, fragmentShader);
	}
	
	public ShaderProgram getShader(){
		return shader;
	}
}
