package deerhunter.entities.systems.attack;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Direction;
import deerhunter.entities.components.Direction.Dir;
import deerhunter.entities.components.attack.MeleeAttack;
import deerhunter.entities.components.Health;
import deerhunter.entities.components.ItemComponent;
import deerhunter.level.Grid;

public class MeleeAttackSystem extends IteratingSystem {
	private ComponentMapper<MeleeAttack> meleeAttackMapper = ComponentMapper.getFor(MeleeAttack.class);
	private ComponentMapper<Direction> directionMapper = ComponentMapper.getFor(Direction.class);
	private ComponentMapper<Bounds> boundMapper = ComponentMapper.getFor(Bounds.class);
	
	private Grid grid;

	public MeleeAttackSystem(Grid grid) {
		super(Family.getFor(MeleeAttack.class, Direction.class, Bounds.class));
		
		this.grid = grid;
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		MeleeAttack meleeAttack = meleeAttackMapper.get(entity);
		Dir direction = directionMapper.get(entity).direction;
		Rectangle rect = boundMapper.get(entity).rect;

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

			// attack
			for (Entity enemy : grid.getEntitiesInRect(attackRect)) {
				if (enemy == entity)
					continue;
				if (enemy.getComponent(Health.class) != null)
					enemy.getComponent(Health.class).injure(meleeAttack.damage);
			}
			
			meleeAttack.attack = false;
		}
	}

}
