package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Velocity;
import deerhunter.level.Grid;

public class MovementSystem extends IteratingSystem {
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);
	private ComponentMapper<Bounds> boundsMapper = ComponentMapper.getFor(Bounds.class);
	
	private Grid grid;

	public MovementSystem(Grid grid) {
		super(Family.getFor(Velocity.class, Position.class, Bounds.class));
		this.grid = grid;
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		Position position = positionMapper.get(entity);
		Velocity velocity = velocityMapper.get(entity);
		Bounds bounds = boundsMapper.get(entity);
		
		Vector2 oldPosition = new Vector2(position.vector);
		position.vector.add(velocity.vector);
		bounds.rect.setPosition(position.vector);
		grid.move(entity, oldPosition, position.vector);
	}
}