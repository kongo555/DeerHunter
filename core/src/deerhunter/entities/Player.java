package deerhunter.entities;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Inventory;
import deerhunter.entities.components.ItemComponent;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Velocity;
import deerhunter.entities.components.attack.MeleeAttack;
import deerhunter.entities.components.attack.RangedAttack;
import deerhunter.level.EntityFactor;
import deerhunter.level.Grid;
import deerhunter.level.Level;

public class Player {
	protected final Entity entity;
	private Level level;
	private EntityFactor entityFactor;
	private Grid grid;
	private Position position;
	private Velocity velocity;
	private MeleeAttack meleeAttack;
	private RangedAttack rangedAttack;
	private Inventory inventory;
	private boolean shoot = false;
	private boolean itemTaken =false;

	private enum Keys {
		LEFT, RIGHT, UP, DOWN, ATTACK, MOUSE_LEFT
	}

	private Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
	{
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.ATTACK, false);
		keys.put(Keys.MOUSE_LEFT, false);
	};

	public Player(Entity entity, Level level, EntityFactor entityFactor, Grid grid) {
		this.entity = entity;
		this.level = level;
		this.entityFactor = entityFactor;
		this.grid = grid;
		position = entity.getComponent(Position.class);
		velocity = entity.getComponent(Velocity.class);
		meleeAttack = entity.getComponent(MeleeAttack.class);
		rangedAttack = entity.getComponent(RangedAttack.class);
		inventory = entity.getComponent(Inventory.class);
	}

	public void processInput(float delta) {
		if (keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) {
		} else if (keys.get(Keys.LEFT)) {
			velocity.vector.x = -1;
		} else if (keys.get(Keys.RIGHT)) {
			velocity.vector.x = 1;
		} else
			velocity.vector.x = 0;

		if (keys.get(Keys.UP) && keys.get(Keys.DOWN)) {
		} else if (keys.get(Keys.UP)) {
			velocity.vector.y = 1;
		} else if (keys.get(Keys.DOWN)) {
			velocity.vector.y = -1;
		} else
			velocity.vector.y = 0;

		velocity.vector.nor().scl(velocity.maxVelocity);
		velocity.vector.scl(delta);

		if (keys.get(Keys.ATTACK)) {
			meleeAttack.attack = true;
			attackReleased();
		}

		if (keys.get(Keys.MOUSE_LEFT)) {
			if (rangedAttack.tension == 0 && takeItem()){
					mouseLeftReleased();
					itemTaken = true;
			}
			else {
				rangedAttack.tension += delta;
				if (shoot == true) {
					rangedAttack.set(new Vector2(level.getMousePosition().x, level.getMousePosition().y));
					mouseLeftReleased();
					shoot = false;
				}
			}
		}
	}
	
	public boolean takeItem() {
		for (Entity item : grid.getEntitiesInRect(new Rectangle(level.getMousePosition().x, level.getMousePosition().y, 1f, 1f))) {
			if (item.getComponent(ItemComponent.class) != null) {
				if ((position.vector.dst(item.getComponent(Position.class).vector) < 3)) {
					inventory.items.add(item.getComponent(ItemComponent.class).item);
					entityFactor.removeEntity(item, item.getComponent(Position.class).vector);;
					return true;
				}
			}
		}
		return false;
	}

	public void shoot() {
		if(!itemTaken)
			shoot = true;
		else
			itemTaken = false;
	}

	public Entity getEntity() {
		return entity;
	}

	public Vector2 getPosition() {
		return entity.getComponent(Position.class).vector;
	}

	// Pressed //
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void upPressed() {
		keys.get(keys.put(Keys.UP, true));
	}

	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}

	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}

	public void downReleased() {
		keys.get(keys.put(Keys.DOWN, false));
	}

	public void attackPressed() {
		keys.get(keys.put(Keys.ATTACK, true));
	}

	public void attackReleased() {
		keys.get(keys.put(Keys.ATTACK, false));
	}

	public void mouseLeftPressed() {
		keys.get(keys.put(Keys.MOUSE_LEFT, true));
	}

	public void mouseLeftReleased() {
		keys.get(keys.put(Keys.MOUSE_LEFT, false));
	}
}
