package deerhunter.entities.systems.attack;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Velocity;
import deerhunter.entities.components.attack.ProjectileMovment;
import deerhunter.utilities.Utility;

public class ProjectileMovementSystem extends IteratingSystem {
	private ComponentMapper<ProjectileMovment> projectileMovmentMapper = ComponentMapper.getFor(ProjectileMovment.class);
	private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);

	public ProjectileMovementSystem() {
		super(Family.getFor(ProjectileMovment.class, Velocity.class));
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		ProjectileMovment projectileMovmentComponent = projectileMovmentMapper.get(entity);
		Vector2 velocity = velocityMapper.get(entity).vector;
		float maxVelocity =  velocityMapper.get(entity).maxVelocity;
		
		velocity.scl(maxVelocity);
		
		projectileMovmentComponent.velocityZ-=0.1;
		if (projectileMovmentComponent.positionZ+projectileMovmentComponent.velocityZ<=1) {
			projectileMovmentComponent.positionZ =1;
			projectileMovmentComponent.landed=true;
		}

		if (projectileMovmentComponent.landed){
			velocity.set(0,0);
			projectileMovmentComponent.velocityZ = 0;
		}
		else
		{
			projectileMovmentComponent.direction = new Vector2(velocity.x,velocity.y).angle();
			projectileMovmentComponent.rotation = new Vector2(velocity.x, velocity.y-(  (int) Math.signum((-1)*velocity.x) )*(Utility.lengthdir_x(projectileMovmentComponent.velocityZ,projectileMovmentComponent.direction * MathUtils.degreesToRadians))).angle();
		}
		
		projectileMovmentComponent.positionZ+=projectileMovmentComponent.velocityZ;
		
		velocity.scl(1/maxVelocity);
	}
}
