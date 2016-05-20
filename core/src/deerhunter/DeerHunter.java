package deerhunter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;

import deerhunter.screens.GameScreen;

public class DeerHunter extends Game {
	@Override
	public void create() {
		setScreen(new GameScreen("level/", new Stage()));
	}
}
