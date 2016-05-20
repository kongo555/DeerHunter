package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Health;
import deerhunter.entities.components.Loot;
import deerhunter.entities.components.Position;
import deerhunter.items.Item;
import deerhunter.level.EntityFactor;

public class HealthSystem extends IteratingSystem {
	private ComponentMapper<Health> healthMapper = ComponentMapper.getFor(Health.class);
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	
	private EntityFactor entityFactor;

	public HealthSystem(EntityFactor entityFactor) {
		super(Family.getFor(Health.class, Position.class));
		this.entityFactor = entityFactor;
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		Health health = healthMapper.get(entity);
		Vector2 position = positionMapper.get(entity).vector;
		
		if(health.currentHP<=0){
			if(entity.getComponent(Loot.class) != null){
				for(Item item : entity.getComponent(Loot.class).items){
					entityFactor.addItem(item.getName(), position, 1, 1, new Rectangle(position.x, position.y , 1, 1), item);
				}
			}
			entityFactor.removeEntity(entity, position);
		}
	}
}