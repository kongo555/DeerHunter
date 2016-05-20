package deerhunter.managers;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class AssetsManager implements Disposable {
	private static AssetManager manager;
	private static String filePath = "textures/";
	private static String png= ".png";
	
	public AssetsManager(String path) {
		manager = new AssetManager();
		load(path);
	}

	public void load(String path){
		ArrayList<String> assetsToLoad = DataManager.loadAssetsList(path);
		for(int i = 0 ;i<assetsToLoad.size();i++)
			manager.load(filePath + assetsToLoad.get(i) + png, Texture.class);
		while (!manager.update()) {
			System.out.println("Loaded: " + manager.getProgress() * 100 + "%");
		}
		 manager.finishLoading();
	}
	
	//TODO usprawnic to bo za kazdym razem tworzy sie nowa a nie wszystkie korzystaja z jednej. Rozwarzyc textureAtlas
	public TextureRegion getTextureRegion(String text){
		return new TextureRegion (getTexture(text));
	}
	
	public Texture getTexture(String text){
		return manager.get(filePath + text + png);
	}
	
	public void resume() {
		// when the game resumes all of the assets need to be reloaded
		manager.finishLoading();
	}

	@Override
	public void dispose() {
		manager.dispose();
	}
}
