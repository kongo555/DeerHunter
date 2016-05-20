package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Direction;
import deerhunter.entities.components.Direction.Dir;
import deerhunter.entities.components.Velocity;

public class DirectionSystem extends IteratingSystem {
	private ComponentMapper<Direction> directionMapper = ComponentMapper.getFor(Direction.class);
	private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);
	
	public DirectionSystem() {
		super(Family.getFor(Direction.class, Velocity.class));
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		Vector2 velocity = velocityMapper.get(entity).vector;
		
		if (velocity.x != 0 || velocity.y != 0) {
			if (velocity.x < 0)
				directionMapper.get(entity).direction = Dir.west;
			if (velocity.x > 0)
				directionMapper.get(entity).direction = Dir.east;
			if (velocity.y < 0)
				directionMapper.get(entity).direction = Dir.south;
			if (velocity.y > 0)
				directionMapper.get(entity).direction = Dir.north;
		}
	}
}