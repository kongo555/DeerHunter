package deerhunter.entities.systems.attack;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import deerhunter.entities.components.Position;
import deerhunter.entities.components.attack.RangedAttack;
import deerhunter.level.EntityFactor;

public class RangedAttackSystem extends IteratingSystem{
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	private ComponentMapper<RangedAttack> rangedAttackMapper = ComponentMapper.getFor(RangedAttack.class);
	
	private EntityFactor entityFactor;

	public RangedAttackSystem(EntityFactor entityFactor) {
		super(Family.getFor(RangedAttack.class, Position.class));
		this.entityFactor = entityFactor;
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		Vector2 position = positionMapper.get(entity).vector;
		RangedAttack rangedAttack = rangedAttackMapper.get(entity);
		if(rangedAttack.shoot == true){
			
			//Vector2 center = new Vector2(position.x + rect.width / 2, position.y + rect.height / 2);
			Vector2 center = new Vector2(position);
			Vector2 direction = rangedAttack.targetPosition.add(center.scl(-1)).nor();
			entityFactor.addProjectile(rangedAttack.name, new Vector2(position), 1, 1, new Rectangle(position.x,position.y ,1,1),direction, rangedAttack.tension, entity, rangedAttack.attackDamage);
			
			
			rangedAttack.reset();
		}
	}
}
