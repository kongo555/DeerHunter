package deerhunter.entities.systems.attack;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Health;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.attack.ProjectileAttack;
import deerhunter.level.EntityFactor;
import deerhunter.level.Grid;
import deerhunter.level.Level;

public class ProjectileAttackSystem extends IteratingSystem {
	private ComponentMapper<ProjectileAttack> projectileAttackMapper = ComponentMapper.getFor(ProjectileAttack.class);
	private ComponentMapper<Bounds> boundsMapper = ComponentMapper.getFor(Bounds.class);
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	
	private EntityFactor entityFactor;
	private Grid grid;

	public ProjectileAttackSystem(EntityFactor entityFactor, Grid grid) {
		super(Family.getFor(ProjectileAttack.class, Bounds.class, Position.class));
		
		this.entityFactor = entityFactor;
		this.grid = grid;
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		ProjectileAttack projectileAttack = projectileAttackMapper.get(entity);
		Rectangle rect = boundsMapper.get(entity).rect;
		Vector2 position = positionMapper.get(entity).vector;
		
		for (Entity enemy : grid.getEntitiesInRect(rect)) {
			if (enemy == entity || enemy == projectileAttack.shooter)
				continue;
			if(enemy.getComponent(Health.class) != null){
				enemy.getComponent(Health.class).injure(projectileAttack.attackDamage);
				entityFactor.removeEntity(entity, position);
			}
		}
	}
}
