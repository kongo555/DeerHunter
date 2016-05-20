package deerhunter.level.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import deerhunter.managers.AssetsManager;

public class TilesData {
	public final Tile[] tiles = new Tile[256];
	//private final Tile grass;

	public TilesData(AssetsManager assetsManager) {
		TextureRegion[][] splitTiles = assetsManager.getTextureRegion("tileset").split(32, 32);
		//grass = new GrassTile(0, splitTiles[0][0]);
		tiles[0] = new Tile(0, splitTiles[0][0], false, false);
		tiles[1] = new Tile(1, splitTiles[0][1], false, true);
		tiles[2] = new Tile(2, splitTiles[1][0], false, false);
		tiles[3] = new Tile(3, splitTiles[1][1], true, false);
	}
}
