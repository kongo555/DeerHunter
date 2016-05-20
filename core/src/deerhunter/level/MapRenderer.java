package deerhunter.level;

import static com.badlogic.gdx.graphics.g2d.Batch.C1;
import static com.badlogic.gdx.graphics.g2d.Batch.C2;
import static com.badlogic.gdx.graphics.g2d.Batch.C3;
import static com.badlogic.gdx.graphics.g2d.Batch.C4;
import static com.badlogic.gdx.graphics.g2d.Batch.U1;
import static com.badlogic.gdx.graphics.g2d.Batch.U2;
import static com.badlogic.gdx.graphics.g2d.Batch.U3;
import static com.badlogic.gdx.graphics.g2d.Batch.U4;
import static com.badlogic.gdx.graphics.g2d.Batch.V1;
import static com.badlogic.gdx.graphics.g2d.Batch.V2;
import static com.badlogic.gdx.graphics.g2d.Batch.V3;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;
import static com.badlogic.gdx.graphics.g2d.Batch.X1;
import static com.badlogic.gdx.graphics.g2d.Batch.X2;
import static com.badlogic.gdx.graphics.g2d.Batch.X3;
import static com.badlogic.gdx.graphics.g2d.Batch.X4;
import static com.badlogic.gdx.graphics.g2d.Batch.Y1;
import static com.badlogic.gdx.graphics.g2d.Batch.Y2;
import static com.badlogic.gdx.graphics.g2d.Batch.Y3;
import static com.badlogic.gdx.graphics.g2d.Batch.Y4;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import deerhunter.level.tiles.Tile;

public class MapRenderer {
	static protected final int NUM_VERTICES = 20;

	private Map map;
	private float unitScale;
	private SpriteBatch batch;
	private Rectangle viewBounds;
	private float vertices[] = new float[NUM_VERTICES];

	final int width;
	final int height;
	final float tileSize;

	public MapRenderer(Map map, float unitScale, Rectangle viewBounds) {
		this.map = map;
		this.unitScale = unitScale;
		this.viewBounds = viewBounds;

		width = map.getWidth();
		height = map.getHeight();
		tileSize = map.getTileSize() * unitScale;
	}

	public void render(SpriteBatch batch) {
		final Color batchColor = batch.getColor();
		final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a);

		final int col1 = Math.max(0, (int) (viewBounds.x / tileSize));
		final int col2 = Math.min(width, (int) ((viewBounds.x + viewBounds.width + tileSize) / tileSize));

		final int row1 = Math.max(0, (int) (viewBounds.y / tileSize));
		final int row2 = Math.min(height, (int) ((viewBounds.y + viewBounds.height + tileSize) / tileSize));

		float y = row2 * tileSize;
		float xStart = col1 * tileSize;
		final float[] vertices = this.vertices;

		for (int row = row2; row >= row1; row--) {
			float x = xStart;
			for (int col = col1; col < col2; col++) {
				final Tile tile = map.getTile(col, row);
				if (tile == null) {
					x += tileSize;
					continue;
				}
				if (tile != null) {
					TextureRegion region = tile.getTextureRegion();

					float x1 = x + 1 * unitScale;
					float y1 = y + 1 * unitScale;
					float x2 = x1 + region.getRegionWidth() * unitScale;
					float y2 = y1 + region.getRegionHeight() * unitScale;

					float u1 = region.getU();
					float v1 = region.getV2();
					float u2 = region.getU2();
					float v2 = region.getV();

					vertices[X1] = x1;
					vertices[Y1] = y1;
					vertices[C1] = color;
					vertices[U1] = u1;
					vertices[V1] = v1;

					vertices[X2] = x1;
					vertices[Y2] = y2;
					vertices[C2] = color;
					vertices[U2] = u1;
					vertices[V2] = v2;

					vertices[X3] = x2;
					vertices[Y3] = y2;
					vertices[C3] = color;
					vertices[U3] = u2;
					vertices[V3] = v2;

					vertices[X4] = x2;
					vertices[Y4] = y1;
					vertices[C4] = color;
					vertices[U4] = u2;
					vertices[V4] = v1;

					batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
				}
				x += tileSize;
			}
			y -= tileSize;
		}
	}
}
