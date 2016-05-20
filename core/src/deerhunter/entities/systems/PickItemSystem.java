package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Inventory;
import deerhunter.entities.components.ItemComponent;
import deerhunter.entities.components.Position;
import deerhunter.level.EntityFactor;
import deerhunter.level.Grid;

public class PickItemSystem extends IteratingSystem {
	private ComponentMapper<Inventory> inventoryMapper = ComponentMapper.getFor(Inventory.class);
	private ComponentMapper<Bounds> boundsMapper = ComponentMapper.getFor(Bounds.class);
	
	private EntityFactor entityFactor;
	private Grid grid;

	public PickItemSystem(EntityFactor entityFactor, Grid grid) {
		super(Family.getFor(Inventory.class, Bounds.class));
		this.entityFactor = entityFactor;
		this.grid = grid;
	}

	@Override
	public void processEntity(Entity entity, float delta) { // TODO uzyc gdyby jakis mob mial zbierac itemy
		Inventory inventory = inventoryMapper.get(entity);
		Rectangle rect = boundsMapper.get(entity).rect;
		
		for (Entity item : grid.getEntitiesInRect(rect)) {
			if (item.getComponent(ItemComponent.class) == null)
				continue;
			inventory.add(item.getComponent(ItemComponent.class).item);
			entityFactor.removeEntity(item, item.getComponent(Position.class).vector);
		}
	}
}