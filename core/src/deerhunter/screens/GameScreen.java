package deerhunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;

import deerhunter.data.Data;
import deerhunter.level.Level;
import deerhunter.managers.AssetsManager;
import deerhunter.utilities.Utility;

public class GameScreen extends AbstractScreen {
	private Data data;
	private AssetsManager assetsManager; 
	private Level level;
	
	public GameScreen(String path ,Stage stage) {
		super(stage);
		data = new Data(path);
		assetsManager = new AssetsManager(path);
		level = new Level(assetsManager, data.mapData);
		Gdx.input.setInputProcessor(this);
	}
	
	
	@Override
	public void update(float delta) {
		level.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.A || keycode == Keys.LEFT)
			level.getPlayer().leftPressed();
		if (keycode == Keys.D || keycode == Keys.RIGHT)
			level.getPlayer().rightPressed();
		if (keycode == Keys.W || keycode == Keys.UP)
			level.getPlayer().upPressed();
		if (keycode == Keys.S || keycode == Keys.DOWN)
			level.getPlayer().downPressed();
		if (keycode == Keys.Z)
			level.getPlayer().attackPressed();
		if (keycode == Keys.Q)
			Utility.printMemoryUsage();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.A || keycode == Keys.LEFT)
			level.getPlayer().leftReleased();
		if (keycode == Keys.D || keycode == Keys.RIGHT)
			level.getPlayer().rightReleased();
		if (keycode == Keys.W || keycode == Keys.UP)
			level.getPlayer().upReleased();
		if (keycode == Keys.S || keycode == Keys.DOWN)
			level.getPlayer().downReleased();
		//if (keycode == Keys.Z)
		//	player.attackReleased();
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT)
			level.getPlayer().mouseLeftPressed();
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT)
			level.getPlayer().shoot();
		return false;
	}
}
