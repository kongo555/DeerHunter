package deerhunter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import deerhunter.DeerHunter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "DeerHunter";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new DeerHunter(), config);
	}
}
