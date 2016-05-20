package deerhunter.entities.systems.visual;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Direction;
import deerhunter.entities.components.Direction.Dir;
import deerhunter.entities.components.attack.MeleeAttack;
import deerhunter.level.Grid;

public class DebugRenderSystem extends EntitySystem {
	private ComponentMapper<Bounds> boundsMapper = ComponentMapper.getFor(Bounds.class);
	private ComponentMapper<MeleeAttack> meleeAttackMapper = ComponentMapper.getFor(MeleeAttack.class);
	private ComponentMapper<Direction> directionMapper = ComponentMapper.getFor(Direction.class);

	private OrthographicCamera camera;
	private Rectangle viewBounds;
	private Grid grid;
	private ArrayList<Entity> entityToRender;
	private ShapeRenderer shapeRenderer;

	public DebugRenderSystem(OrthographicCamera camera, Grid grid, Rectangle viewBounds) {
		this.camera = camera;
		this.grid = grid;
		this.viewBounds = viewBounds;

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void addedToEngine(Engine engine) {
	}

	@Override
	public void removedFromEngine(Engine engine) {
	}

	@Override
	public void update(float deltaTime) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(new Color(1, 1, 0, 1));

		entityToRender = grid.getEntitiesInRect(viewBounds);

		for (int i = 0; i < entityToRender.size(); ++i) {
			Entity entity = entityToRender.get(i);
			Rectangle rect = boundsMapper.get(entity).rect;

			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}

		for (int i = 0; i < entityToRender.size(); ++i) {
			Entity entity = entityToRender.get(i);
			if (!(entity.getComponent(MeleeAttack.class) == null)) {
				MeleeAttack meleeAttack = meleeAttackMapper.get(entity);
				Dir direction = directionMapper.get(entity).direction;
				Rectangle rect = boundsMapper.get(entity).rect;

				if (meleeAttack.attack) {
					Rectangle attackRect = new Rectangle();
					if (direction == Dir.north) {
						attackRect.width = meleeAttack.lenght;
						attackRect.height = meleeAttack.range;
						attackRect.x = rect.x - meleeAttack.lenght0;
						attackRect.y = rect.y + rect.height - meleeAttack.range0;
					} else if (direction == Dir.south) {
						attackRect.width = meleeAttack.lenght;
						attackRect.height = meleeAttack.range;
						attackRect.x = rect.x - meleeAttack.lenght0;
						attackRect.y = rect.y - attackRect.height + meleeAttack.range0;
					} else if (direction == Dir.west) {
						attackRect.width = meleeAttack.range;
						attackRect.height = meleeAttack.lenght;
						attackRect.x = rect.x - attackRect.width + meleeAttack.range0;
						attackRect.y = rect.y - meleeAttack.lenght0;
					} else if (direction == Dir.east) {
						attackRect.width = meleeAttack.range;
						attackRect.height = meleeAttack.lenght;
						attackRect.x = rect.x + rect.width - meleeAttack.range0;
						attackRect.y = rect.y - meleeAttack.lenght0;
					}

					shapeRenderer.rect(attackRect.x, attackRect.y, attackRect.width, attackRect.height);
				}
			}
		}

		shapeRenderer.end();
	}

}