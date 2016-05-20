package deerhunter.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Utility {
	
	public static void printMemoryUsage() {
		 System.gc();
		 int mb = 1024 * 1024;
		 //Getting the runtime reference from system
		 Runtime runtime = Runtime.getRuntime();
		 System.out.println("##### Heap utilization statistics [MB] #####");
		 //Print used memory
		 System.out.println("Used Memory:"
		 + (runtime.totalMemory() - runtime.freeMemory()) / mb);
		 //Print free memory
		 System.out.println("Free Memory:"
		 + runtime.freeMemory() / mb);
		 //Print total available memory
		 System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		 //Print Maximum available memory
		 System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		 }
	
	public static float lengthdir_x(float len,float dir){
		return MathUtils.cos(dir) * len;
	}
	
	public static float lengthdir_y(float len,float dir){
		return MathUtils.sin(dir) * len;
	}
	
	public static ShaderProgram loadShader(String vert, String frag) {
		ShaderProgram prog = new ShaderProgram(vert, frag);
		if (!prog.isCompiled())
			throw new GdxRuntimeException("could not compile shader: " + prog.getLog());
		if (prog.getLog().length() != 0)
			Gdx.app.log("GpuShadows", prog.getLog());
		return prog;
	}
}
