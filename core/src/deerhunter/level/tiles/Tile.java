package deerhunter.level.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
	private final int id;
	private TextureRegion textureRegion;
	private boolean collision;
	private boolean liquid;
	
	public Tile(int id, TextureRegion textureRegion, boolean collision, boolean liquid) {
		this.id = id;
		this.textureRegion = textureRegion;
		this.collision = collision;
		this.liquid = liquid;
	}
	
	public int getId(){
		return id;
	}
	
	public TextureRegion getTextureRegion(){
		return textureRegion;
	}
	
	public boolean isCollision(){
		return collision;
	}
	public boolean isLiquid(){
		return liquid;
	}
}
