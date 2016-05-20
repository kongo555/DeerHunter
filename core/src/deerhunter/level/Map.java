package deerhunter.level;

import deerhunter.data.MapData;
import deerhunter.level.tiles.Tile;
import deerhunter.level.tiles.TilesData;
import deerhunter.managers.AssetsManager;

public class Map {
	private int width;
	private int height;
	private int tileSize;
	
	private int level[];
	
	TilesData tilesData;
	
	
	public Map(MapData mapData, AssetsManager resourceManager){
		width = mapData.width;
		height = mapData.height;
		tileSize = 32;
		tilesData = new TilesData(resourceManager);
		
		level = mapData.map;
		
		//width = 1000;
		//height = 1000;
		//level = new int[width*height];
		//for(int i =0;i <width*height ; i++)
		//	level[i]=0;
	}
	
	public int getWidth () {
		return width;
	}

	public int getHeight () {
		return height;
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width) return null;
		if (y < 0 || y >= height) return null;
		return tilesData.tiles[level[x +y * width]];
	}
	
	public float getTileSize () {
		return tileSize;
	}
}
