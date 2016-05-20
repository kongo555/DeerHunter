package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

import deerhunter.entities.components.AI;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Velocity;
import deerhunter.utilities.AStar;

public class AISystem extends IteratingSystem {
	private ComponentMapper<AI> AIMapper = ComponentMapper.getFor(AI.class);
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);

	private Vector2 playerPosition;
	private AStar aStar;

	public AISystem(Vector2 playerPosition, AStar aStar) {
		super(Family.getFor(AI.class, Position.class, Velocity.class));
		this.playerPosition = playerPosition;
		this.aStar = aStar;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		AI aiComponent = AIMapper.get(entity);
		Position positionComponent = positionMapper.get(entity);
		Velocity VelocityComponent = velocityMapper.get(entity);

		Vector2 position = new Vector2(positionComponent.vector);
		Vector2 playerPosition = new Vector2(this.playerPosition);
		/*
		 * int range = 0;
		 * float distance = playerPosition.dst(position);
		 * if (distance > range && distance < aiComponent.maxDistance) {
		 * Vector2 direction = playerPosition.add(position.scl(-1));
		 * direction.nor();
		 * VelocityComponent.vector = direction;
		 * VelocityComponent.vector.nor().scl(VelocityComponent.maxVelocity);
		 * VelocityComponent.vector.scl(deltaTime);
		 * } else {
		 * VelocityComponent.vector.set(0,0);
		 * }
		 */

		int range = 0;
		float distance = playerPosition.dst(position);
		if (distance > range && distance < aiComponent.maxDistance) {
			IntArray path = aStar.getPath(entity, (int) position.x, (int) position.y, (int) playerPosition.x, (int) playerPosition.y);
			if (path.size > 3) {
				Vector2 direction = new Vector2(path.get(path.size-4),path.get(path.size-3));
				direction.add(position.scl(-1));
				direction.nor();
				VelocityComponent.vector = direction;
				VelocityComponent.vector.nor().scl(VelocityComponent.maxVelocity);
				VelocityComponent.vector.scl(deltaTime);
			}
		} else {
			VelocityComponent.vector.set(0, 0);
		}
	}
}
