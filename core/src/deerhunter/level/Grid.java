package deerhunter.level;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Bounds;

public class Grid {
	public int cellSize = 2;
	public int width, height;
	public ArrayList<Entity>[] entitiesInCell;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		entitiesInCell = new ArrayList[(width / cellSize) * (height / cellSize)];
		for (int i = 0; i < (width / cellSize) * (height / cellSize); i++) {
			entitiesInCell[i] = new ArrayList<Entity>();
		}
	}

	public void insertEntity(Entity entity, int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return;
		entitiesInCell[x + y * (width / cellSize)].add(entity);
	}

	public void removeEntity(Entity entity, int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return;
		entitiesInCell[x + y * (width / cellSize)].remove(entity);
	}

	public void move(Entity entity, Vector2 oldPosition, Vector2 position) {
		// See which cell it was in.
		int oldCellX = (int) (oldPosition.x / cellSize);
		int oldCellY = (int) (oldPosition.y / cellSize);

		// See which cell it's moving to.
		int cellX = (int) (position.x / cellSize);
		int cellY = (int) (position.y / cellSize);

		// If it didn't change cells, we're done.
		if (oldCellX == cellX && oldCellY == cellY)
			return;

		// Unlink it from the list of its old cell.
		removeEntity(entity, oldCellX, oldCellY);

		// Add it back to the grid at its new cell.
		insertEntity(entity, cellX, cellY);
	}

	public ArrayList<Entity> getEntities(Vector2 position) {
		ArrayList<Entity> entities = new ArrayList<Entity>();

		int cellX = (int) (position.x / cellSize);
		int cellY = (int) (position.y / cellSize);

		entities.addAll(entitiesInCell[cellX + cellY * (width / cellSize)]);
		if (position.x > 0 && position.y > 0)
			entities.addAll(entitiesInCell[cellX - 1 + (cellY - 1) * (width / cellSize)]);
		if (position.x > 0)
			entities.addAll(entitiesInCell[cellX - 1 + cellY * (width / cellSize)]);
		if (position.y > 0)
			entities.addAll(entitiesInCell[cellX + (cellY - 1) * (width / cellSize)]);
		if (position.x > 0 && position.y < (height / cellSize) - 1)
			entities.addAll(entitiesInCell[cellX - 1 + (cellY + 1) * (width / cellSize)]);

		return entities;
	}

	public ArrayList<Entity> getEntitiesInRect(Rectangle rect) {
		ArrayList<Entity> result = new ArrayList<Entity>();
		int xt0 = (int) (rect.x / cellSize);
		int yt0 = (int) (rect.y / cellSize);
		int xt1 = (int) ((rect.x + rect.width) / cellSize);
		int yt1 = (int) ((rect.y + rect.height) / cellSize);
		for (int y = yt0; y <= yt1; y++) {
			for (int x = xt0; x <= xt1; x++) {
				if (x < 0 || y < 0 || x >= width / cellSize || y >= height / cellSize)
					continue;
				for (Entity entity : entitiesInCell[x + y * (width / cellSize)]) {
					Rectangle entityRect = entity.getComponent(Bounds.class).rect;
					if (rect.overlaps(entityRect))
						result.add(entity);
				}
			}
		}
		return result;
	}
	
	
}
