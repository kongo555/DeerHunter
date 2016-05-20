package deerhunter.entities.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Collision;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Velocity;
import deerhunter.level.Grid;
import deerhunter.level.Map;
import deerhunter.level.tiles.Tile;

public class CollisionSystem extends IteratingSystem {
	private ComponentMapper<Bounds> boundsMapper = ComponentMapper.getFor(Bounds.class);

	private Map map;
	private Grid grid;
	private Array<Rectangle> tiles;
	private ArrayList<Entity> staticEntities;

	public CollisionSystem(Map map, Grid grid) {
		super(Family.getFor(Collision.class, Bounds.class));

		this.map = map;
		this.grid = grid;
		tiles = new Array<Rectangle>();
		staticEntities = new ArrayList<Entity>();
	}

	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	@Override
	public void processEntity(Entity entity, float delta) {
		Rectangle rect = boundsMapper.get(entity).rect;

		Rectangle collisionRect = rectPool.obtain();
		collisionRect.set(rect);

		if (entity.getComponent(Velocity.class) != null)
			dynamicEntity(entity, entity.getComponent(Velocity.class).vector, rect, collisionRect);
		else
			staticEntity(entity, rect, collisionRect);

		rectPool.free(collisionRect);
	}

	private void dynamicEntity(Entity entity, Vector2 velocity, Rectangle rect, Rectangle collisionRect) {
		// // tile x // //
		int startX, startY, endX, endY;
		if (velocity.x > 0) {
			startX = endX = (int) (rect.x + rect.width + velocity.x);
		} else {
			startX = endX = (int) (rect.x + velocity.x);
		}
		startY = (int) (rect.y);
		endY = (int) (rect.y + rect.height);

		getTiles(startX, startY, endX, endY, tiles);
		collisionRect.x += velocity.x;
		for (Rectangle tile : tiles) {
			if (collisionRect.overlaps(tile)) {
				velocity.x = 0;
				break;
			}
		}
		collisionRect.x = rect.x;

		// // tile y // //
		if (velocity.y > 0) {
			startY = endY = (int) (rect.y + rect.height + velocity.y);
		} else {
			startY = endY = (int) (rect.y + velocity.y);
		}
		startX = (int) (rect.x);
		endX = (int) (rect.x + rect.width);
		getTiles(startX, startY, endX, endY, tiles);

		collisionRect.y += velocity.y;

		for (Rectangle tile : tiles) {
			if (collisionRect.overlaps(tile)) {
				velocity.y = 0;
				break;
			}
		}
		collisionRect.y = rect.y;

		Rectangle collisionRect2 = rectPool.obtain();
		for (Entity entity2 : grid.getEntities(new Vector2(rect.x, rect.y))) {
			if (entity == entity2)
				continue;
			if (entity2.getComponent(Collision.class) == null)
				continue;
			if (entity2.getComponent(Velocity.class) == null) {
				staticEntities.add(entity2);
				continue;
			}

			Vector2 velocity2 = entity2.getComponent(Velocity.class).vector;
			Rectangle rect2 = entity2.getComponent(Bounds.class).rect;

			collisionRect2.set(rect2);

			// // entity x // //
			collisionRect.x += velocity.x;
			collisionRect2.x += velocity2.x;
			if (collisionRect.overlaps(collisionRect2)) {
				if (velocity.x * velocity2.x <= 0) {
					velocity.x = 0;
					velocity2.x = 0;
				} else if (rect.x > rect2.x) {
					if (velocity.x > 0) {
						velocity2.x = 0;
					} else {
						velocity.x = 0;
					}
				}
			}
			collisionRect.x = rect.x;
			collisionRect2.x = rect2.x;

			// // entity y // //
			collisionRect.y += velocity.y;
			collisionRect2.y += velocity2.y;
			if (collisionRect.overlaps(collisionRect2)) {
				if (velocity.y * velocity2.y <= 0) {
					velocity.y = 0;
					velocity2.y = 0;
				} else if (rect.y > rect2.y) {
					if (velocity.y > 0) {
						velocity2.y = 0;
					} else {
						velocity.y = 0;
					}
				}
			}
			collisionRect.y = rect.y;
		}
		for (Entity entity2 : staticEntities) {
			Rectangle rect2 = entity2.getComponent(Bounds.class).rect;

			collisionRect2.set(rect2);

			// // entity x // //
			collisionRect.x += velocity.x;
			if (collisionRect.overlaps(collisionRect2)) {
				velocity.x = 0;
			}
			collisionRect.x = rect.x;

			// // entity y // //
			collisionRect.y += velocity.y;
			if (collisionRect.overlaps(collisionRect2)) {
				velocity.y = 0;
			}
			collisionRect.y = rect.y;

			rectPool.free(collisionRect2);
		}
		staticEntities.clear();
	}

	private void staticEntity(Entity entity, Rectangle rect, Rectangle collisionRect) {
		Rectangle collisionRect2 = rectPool.obtain();
		for (Entity entity2 : grid.getEntities(new Vector2(rect.x, rect.y))) {
			if (entity == entity2)
				continue;
			if (entity2.getComponent(Collision.class) == null)
				continue;
			if (entity2.getComponent(Velocity.class) == null) {
				continue;
			}

			Vector2 velocity2 = entity2.getComponent(Velocity.class).vector;
			Rectangle rect2 = entity2.getComponent(Bounds.class).rect;

			collisionRect2.set(rect2);

			// // entity x // //
			collisionRect2.x += velocity2.x;
			if (collisionRect.overlaps(collisionRect2)) {
				velocity2.x = 0;
			}
			collisionRect2.x = rect2.x;

			// // entity y // //
			collisionRect2.y += velocity2.y;
			if (collisionRect.overlaps(collisionRect2)) {
				velocity2.y = 0;
			}
		}
		rectPool.free(collisionRect2);
	}

	private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Tile tile = map.getTile(x, y);
				if (tile == null)
					continue;
				if (tile.isCollision()) {
					Rectangle rect = rectPool.obtain();
					rect.set(x, y, 1, 1);
					tiles.add(rect);
				}
			}
		}
	}
}